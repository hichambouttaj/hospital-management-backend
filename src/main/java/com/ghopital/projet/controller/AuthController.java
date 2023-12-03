package com.ghopital.projet.controller;

import com.ghopital.projet.auth.UserModel;
import com.ghopital.projet.dto.*;
import com.ghopital.projet.dto.request.LoginRequestDto;
import com.ghopital.projet.dto.request.UpdateUserPasswordDto;
import com.ghopital.projet.dto.response.JwtAuthResponse;
import com.ghopital.projet.dto.response.UserDtoResponse;
import com.ghopital.projet.service.AuthService;
import com.ghopital.projet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    // build login
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> loginHandler(
            @Valid @RequestBody LoginRequestDto loginDto) {
        String token = authService.login(loginDto);

        JwtAuthResponse response = new JwtAuthResponse(token, TokenType.Bearer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // update password by user
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/password")
    public ResponseEntity<String> updatePassword(
            @AuthenticationPrincipal UserModel userModel,
            @Valid @RequestBody UpdateUserPasswordDto passwordDto) {
        String response = userService.updatePassword(userModel.getUsername(), passwordDto);
        return ResponseEntity.ok(response);
    }

    // get info authenticated user
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/info")
    public ResponseEntity<UserDtoResponse> getCurrentUser(
            @AuthenticationPrincipal UserModel userModel) {
        return ResponseEntity.ok(userService.getUserByCin(userModel.getUsername()));
    }
}
