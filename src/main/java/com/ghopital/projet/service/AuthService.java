package com.ghopital.projet.service;


import com.ghopital.projet.dto.request.LoginRequestDto;

public interface AuthService {
    String login(LoginRequestDto loginDto);
}
