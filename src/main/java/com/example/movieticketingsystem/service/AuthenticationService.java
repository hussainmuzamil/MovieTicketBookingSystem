package com.example.movieticketingsystem.service;

import com.example.movieticketingsystem.entity.AuthenticationRequest;
import com.example.movieticketingsystem.entity.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface AuthenticationService {
    public ResponseEntity<Object> signIn(AuthenticationRequest authRequest);
    public ResponseEntity<Object> registerUser(SignUpRequest signUpRequest);

    void refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;
}
