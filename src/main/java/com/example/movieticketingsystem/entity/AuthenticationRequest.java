package com.example.movieticketingsystem.entity;


import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationRequest {
    private String email;
    private String password;
}
