package com.ghopital.projet.dto.response;

import com.ghopital.projet.dto.TokenType;

public record JwtAuthResponse (String token, TokenType type){
}
