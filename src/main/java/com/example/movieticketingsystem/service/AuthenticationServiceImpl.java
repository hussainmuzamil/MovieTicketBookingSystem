package com.example.movieticketingsystem.service;

import com.example.movieticketingsystem.config.JWTService;
import com.example.movieticketingsystem.dto.AuthenticationRequest;
import com.example.movieticketingsystem.dto.JwtAuthenticationResponse;
import com.example.movieticketingsystem.dto.SignUpRequest;
import com.example.movieticketingsystem.dto.VerificationRequest;
import com.example.movieticketingsystem.entity.*;
import com.example.movieticketingsystem.repository.RoleRepository;
import com.example.movieticketingsystem.repository.TokenRepository;
import com.example.movieticketingsystem.repository.UserRepository;
import com.example.movieticketingsystem.tfa.TwoFactorAuthenticationService;
import com.example.movieticketingsystem.util.ResponseHandler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final TokenRepository tokenRepository;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository repository;
    private final TwoFactorAuthenticationService twoFactorAuthenticationService;
    @Override
    public ResponseEntity<Object> signIn(AuthenticationRequest authRequest) {
        if(authRequest == null){
            log.error("Request Body shouldn't be empty");

            return ResponseHandler.responseBuilder("Request body is empty", HttpStatus.BAD_REQUEST,null,null);
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),authRequest.getPassword()));
        var user = userRepository.findAppUserByEmail(authRequest.getEmail()).get();
        if(user == null){
            log.error("No user found with username {}",authRequest.getEmail());
            return ResponseHandler.responseBuilder("No user found ",HttpStatus.NOT_FOUND,null,null);
        }
        log.info("generating jwt token");
        var jwt = jwtService.generateToken(user);
        if(jwt == null) {
            log.warn("Unable to create jwt token with username {}",authRequest.getEmail());
        }
        log.info("Saving user token");
        revokeAllUserTokens(user);
        saveUserToken(user,jwt);
        log.info("generating refresh token");
        var refreshToken = jwtService.regenerateToken(new HashMap<>(),user);
        if(refreshToken == null){
            log.warn("unable to regenerate token with username {}",authRequest.getEmail());
        }
        JwtAuthenticationResponse response = new JwtAuthenticationResponse();
        if(user.isMfaEnabled()){
            response = JwtAuthenticationResponse.builder()
                    .refreshToken("")
                    .token("")
                    .mfaEnabled(user.isMfaEnabled())
                    .secret(user.getSecret())
                    .build();
        }
        response = JwtAuthenticationResponse.builder()
                .refreshToken(refreshToken)
                .token(jwt)
                .mfaEnabled(user.isMfaEnabled())
                .build();
        log.info("got authentication response");
        return ResponseHandler.responseBuilder("auth response",HttpStatus.OK,response,linkTo(AppUser.class).slash(user.getId()).withSelfRel());
    }

    @Override
    public ResponseEntity<Object> registerUser(SignUpRequest signUpRequest) {
        if(signUpRequest == null){
            log.error("Request body is empty");
            return ResponseHandler.responseBuilder("Request body shouldn't be empty",HttpStatus.BAD_REQUEST,null,null);
        }
//        AppUser u2 = userRepository.findAppUserByEmail(principalName).get();
        AppUser u1 = new AppUser();
        u1.setEmail(signUpRequest.getEmail());
        u1.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
//        Role role = roleRepository.findById(userRequest.getRole().getId()).get();
        log.info("password is {}",signUpRequest.getPassword());
        Role role = repository.findByName("USER").get();
        u1.setRole(role);
        u1.setActive(true);
        u1.setMfaEnabled(signUpRequest.isMfaEnabled());
        if(signUpRequest.isMfaEnabled()){
            u1.setSecret(twoFactorAuthenticationService.generateNewSecret());
        }
//        u1.setAddedBy(u2);

        log.info("saving user authentication info");
        AppUser checkAuthentication = userRepository.save(u1);
        if(checkAuthentication == null){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong in saving user");
        }
        log.info("saved user authentication");
        var jwtToken = jwtService.generateToken(u1);
        var refreshToken = jwtService.regenerateToken(new HashMap<>(),checkAuthentication);
        var response = JwtAuthenticationResponse.builder()
                .refreshToken(refreshToken)
                .token(jwtToken)
                .mfaEnabled(checkAuthentication.isMfaEnabled())
                .secret(checkAuthentication.getSecret())
                .build();
        return ResponseHandler.responseBuilder("Successfully created User",HttpStatus.CREATED,response,linkTo(AppUser.class).slash(u1.getId()).withSelfRel());

    }

    @Override
    public void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {

    }

    @Override
    public ResponseEntity<Object> verifyCode(VerificationRequest verificationRequest) {
        AppUser user = userRepository.findAppUserByEmail(verificationRequest.getEmail())
                .orElseThrow(()->new EntityNotFoundException(String.format("No user exist %s",verificationRequest.getEmail())));
        if(twoFactorAuthenticationService.isOtpNotValid(user.getSecret(), verificationRequest.getOtpCode())){
            throw new BadCredentialsException("Code is not correct");
        }else{
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.regenerateToken(new HashMap<>(),user);
            return ResponseHandler.responseBuilder("token generated",HttpStatus.OK,JwtAuthenticationResponse.builder()
                    .token(jwtToken)
                    .mfaEnabled(user.isMfaEnabled())
                    .refreshToken(refreshToken)
                    .build(),linkTo(AppUser.class).slash(user.getId()).withSelfRel());
        }
    }

    private void saveUserToken(AppUser checkAppUser,String jwtToken) {
        var token = Token.builder()
                .appUser(checkAppUser)
                .tokenType(TokenType.BEARER)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        var savedToken = tokenRepository.save(token);
    }

    private void revokeAllUserTokens(AppUser appUser){
        if(tokenRepository.findAll().isEmpty()){
            return;
        }else{
            var validTokens = tokenRepository.findAllValidTokenByUser(appUser);
            if(validTokens.isEmpty()){
                return;
            }
            validTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validTokens);
        }
    }
}
