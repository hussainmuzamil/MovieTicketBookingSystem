package com.example.movieticketingsystem.config;

import com.example.movieticketingsystem.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(StringUtils.isEmpty(header) || !StringUtils.startsWith(header,"Bearer")){
            filterChain.doFilter(request,response);

            logger.info("header doesn't contain bearer token");
            return;
        }
        jwt = header.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        if(!StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            var istTokenValid = tokenRepository.findByToken(jwt).map(token -> !token.isRevoked() && !token.isExpired()).orElse(false);
            if (jwtService.isTokenValid(jwt,userDetails) && istTokenValid){
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities()
                );
                context.setAuthentication(token);
                SecurityContextHolder.setContext(context);
            }else{
                logger.info("Validation Fails");
            }
        }
        filterChain.doFilter(request,response);
    }
}
