package com.ghopital.projet.service;

import com.ghopital.projet.dto.request.ChangePasswordDto;
import com.ghopital.projet.dto.request.UpdateUserPasswordDto;
import com.ghopital.projet.dto.request.UserDtoRequest;
import com.ghopital.projet.dto.request.UserUpdateDtoRequest;
import com.ghopital.projet.dto.response.UserDtoResponse;

import java.util.List;

public interface UserService {
    UserDtoResponse createUser(UserDtoRequest userDtoRequest);
    UserDtoResponse getUserById(Long userId);
    UserDtoResponse getUserByCin(String cin);
    UserDtoResponse getUserByEmail(String email);
    List<UserDtoResponse> getAllUsers();
    UserDtoResponse updateUser(UserUpdateDtoRequest userDtoRequest, Long userId);
    String updatePassword(String cin, UpdateUserPasswordDto updateUserPasswordDto);
    UserDtoResponse updatePassword(ChangePasswordDto passwordDto, Long userId);
    UserDtoResponse addImageToUser(Long userId, Long imageId);
    void deleteUser(Long userId);
}
