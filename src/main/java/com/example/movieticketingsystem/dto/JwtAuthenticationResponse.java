package com.example.movieticketingsystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JwtAuthenticationResponse {

    private String token;
    private String refreshToken;
    private boolean mfaEnabled;
    private String secret;
}
