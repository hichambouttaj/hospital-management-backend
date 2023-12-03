package com.ghopital.projet.controller;

import com.ghopital.projet.dto.request.ChangePasswordDto;
import com.ghopital.projet.dto.request.ImageToUserDto;
import com.ghopital.projet.dto.request.UserDtoRequest;
import com.ghopital.projet.dto.request.UserUpdateDtoRequest;
import com.ghopital.projet.dto.response.UserDtoResponse;
import com.ghopital.projet.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(params = {"cin"})
    public ResponseEntity<UserDtoResponse> getByCin(@RequestParam String cin) {
        return ResponseEntity.ok(userService.getUserByCin(cin));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDtoResponse> getById(@PathVariable(name = "id") Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping
    public ResponseEntity<UserDtoResponse> createUser(@Valid @RequestBody UserDtoRequest userDto) {
        UserDtoResponse user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDtoResponse> updateUser(
            @Valid @RequestBody UserUpdateDtoRequest userDto,
            @PathVariable(name = "id") long id) {
        UserDtoResponse response = userService.updateUser(userDto, id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<UserDtoResponse> updatePassword(
            @Valid @RequestBody ChangePasswordDto passwordDto,
            @PathVariable(name = "id") Long id) {
        UserDtoResponse response = userService.updatePassword(passwordDto, id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/images")
    public ResponseEntity<UserDtoResponse> addImageToUser(
            @PathVariable(name = "id") Long id, @Valid @RequestBody ImageToUserDto imageToUserDto) {
        UserDtoResponse response = userService.addImageToUser(id, imageToUserDto.imageId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "id") long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}
