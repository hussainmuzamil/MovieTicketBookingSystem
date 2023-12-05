package com.example.movieticketingsystem.config;

import com.example.movieticketingsystem.repository.TokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class LogoutHandlerService implements LogoutSuccessHandler {
    private final TokenRepository tokenRepository;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        final String jwt;
        final String userEmail;
        if(StringUtils.isEmpty(header) || !StringUtils.startsWith(header,"Bearer")){
//            filterChain.doFilter(request,response);

            log.info("header doesn't contain bearer token");
            return;
        }

        jwt = header.substring(7);
        var storedToken = tokenRepository.findByToken(jwt).orElse(null);
        if(storedToken == null) {
            return;
        }else{
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        }
    }
}
