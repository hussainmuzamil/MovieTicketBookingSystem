package com.example.movieticketingsystem.controller;

import com.example.movieticketingsystem.dto.AuthenticationRequest;
import com.example.movieticketingsystem.dto.SignUpRequest;
import com.example.movieticketingsystem.dto.VerificationRequest;
import com.example.movieticketingsystem.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    @PostMapping("/registration")
    public ResponseEntity<Object> signUp(@RequestBody SignUpRequest signUpRequest){
        return authenticationService.registerUser(signUpRequest);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<Object> login(@RequestBody AuthenticationRequest authenticationRequest){
        return authenticationService.signIn(authenticationRequest);
    }
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse
    ) throws IOException {
        authenticationService.refreshToken(httpServletRequest,httpServletResponse);
    }
    @PostMapping("/verify")
    public ResponseEntity<Object> verifyCode(@RequestBody VerificationRequest verificationRequest) {
        return authenticationService.verifyCode(verificationRequest);
    }
}
