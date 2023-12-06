package com.example.movieticketingsystem.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VerificationRequest {
    private String email;
    private String otpCode;
}
