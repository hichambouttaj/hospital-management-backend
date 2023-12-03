package com.ghopital.projet.service.impl;

import com.ghopital.projet.auth.AuthenticationFacade;
import com.ghopital.projet.dto.ROLES;
import com.ghopital.projet.dto.request.ChangePasswordDto;
import com.ghopital.projet.dto.request.UpdateUserPasswordDto;
import com.ghopital.projet.dto.request.UserDtoRequest;
import com.ghopital.projet.dto.request.UserUpdateDtoRequest;
import com.ghopital.projet.dto.response.UserDtoResponse;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.entity.Role;
import com.ghopital.projet.entity.User;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.mapper.UserMapper;
import com.ghopital.projet.repository.FileRepository;
import com.ghopital.projet.repository.RoleRepository;
import com.ghopital.projet.repository.UserRepository;
import com.ghopital.projet.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;
    private final FileRepository fileRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, AuthenticationFacade authenticationFacade,
                           FileRepository fileRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = authenticationFacade;
        this.fileRepository = fileRepository;

    }

    @Override
    @Transactional
    public UserDtoResponse createUser(UserDtoRequest userDtoRequest) {
        // check if already exist in db
        if(userRepository.existsByCin(userDtoRequest.cin()) || userRepository.existsByEmail(userDtoRequest.email())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "User already exist");
        }

        // convert DTO to entity
        User user = UserMapper.INSTANCE.userDtoRequestToUser(userDtoRequest);

        // encode password
        user.setPassword(passwordEncoder.encode(userDtoRequest.password()));

        // add roles to user entity
        addRoleToUser(user, userDtoRequest.role().name());

        // save user to db
        User savedUser = userRepository.save(user);

        return UserMapper.INSTANCE.userToUserDtoResponse(savedUser);
    }

    @Override
    public UserDtoResponse getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId.toString())
        );

        return UserMapper.INSTANCE.userToUserDtoResponse(user);
    }

    @Override
    public UserDtoResponse getUserByCin(String cin) {
        User user = userRepository.findByCin(cin).orElseThrow(
                () -> new ResourceNotFoundException("User", "cin", cin)
        );

        return UserMapper.INSTANCE.userToUserDtoResponse(user);
    }

    @Override
    public UserDtoResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );
        return UserMapper.INSTANCE.userToUserDtoResponse(user);
    }

    @Override
    public List<UserDtoResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper.INSTANCE::userToUserDtoResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserDtoResponse updateUser(UserUpdateDtoRequest userDtoRequest, Long userId) {
        // get user by id from db
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId.toString())
        );

        if(StringUtils.hasText(userDtoRequest.firstName())) {
            user.setFirstName(userDtoRequest.firstName());
        }

        if(StringUtils.hasText(userDtoRequest.lastName())) {
            user.setLastName(userDtoRequest.lastName());
        }

        if(StringUtils.hasText(userDtoRequest.cin())) {
            user.setCin(userDtoRequest.cin());
        }

        if(StringUtils.hasText(userDtoRequest.email())) {
            user.setEmail(userDtoRequest.email());
        }

        if(userDtoRequest.role() != null && StringUtils.hasText(userDtoRequest.role().name())) {
            addRoleToUser(user, userDtoRequest.role().name());
        }

        // update user in db
        User updatedUser = userRepository.save(user);

        return UserMapper.INSTANCE.userToUserDtoResponse(updatedUser);
    }

    @Override
    @Transactional
    public UserDtoResponse updatePassword(ChangePasswordDto passwordDto, Long userId) {
        // get user from db
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId.toString())
        );

        // validate permission
        if(!isAdmin())
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid credential");

        if(!passwordDto.newPassword().equals(passwordDto.confirmNewPassword()))
            throw new AppException(HttpStatus.BAD_REQUEST, "Invalid input");

        user.setPassword(passwordEncoder.encode(passwordDto.newPassword()));
        User updatedUser = userRepository.save(user);

        return UserMapper.INSTANCE.userToUserDtoResponse(updatedUser);
    }

    @Override
    @Transactional
    public String updatePassword(String cin, UpdateUserPasswordDto updateUserPasswordDto) {
        // get user from db
        User user = userRepository.findByCin(cin).orElseThrow(
                () -> new ResourceNotFoundException("User", "cin", cin)
        );

        // check permission
        if(!authenticationFacade.getAuthentication().getName().equals(user.getCin())) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid credential");
        }

        // check if not same old password
        if(!passwordEncoder.matches(updateUserPasswordDto.oldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect old password");
        }

        // check new and old password
        if(!updateUserPasswordDto.oldPassword().equals(updateUserPasswordDto.newPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Password should not match the old one");
        }

        if(!updateUserPasswordDto.newPassword().equals(updateUserPasswordDto.confirmNewPassword())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Password should be the same");
        }

        user.setPassword(passwordEncoder.encode(updateUserPasswordDto.newPassword()));
        userRepository.save(user);

        return "Password changed successfully";
    }

    @Override
    @Transactional
    public UserDtoResponse addImageToUser(Long userId, Long imageId) {
        // get user from db
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId.toString())
        );

        // check logged user
        String loggedUserCin = authenticationFacade.getAuthentication().getName();
        if(!user.getCin().equals(loggedUserCin)) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid credential");
        }

        // get image from db
        File image = fileRepository.findById(imageId).orElseThrow(
                () -> new ResourceNotFoundException("Image", "id", imageId.toString())
        );

        // add image to user
        user.setImage(image);

        // update user in db
        User updatedUser = userRepository.save(user);

        return UserMapper.INSTANCE.userToUserDtoResponse(updatedUser);
    }

    @Override
    public void deleteUser(Long userId) {
        // get user by id from db
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId.toString())
        );

        userRepository.delete(user);
    }

    private void addRoleToUser(User user, String roleName) {
        // get role from db
        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> new ResourceNotFoundException("Role", "name", roleName)
        );

        // check if admin before add admin role
        if(role.getName().equals(ROLES.ROLE_ADMIN.name()) && !isAdmin())
            throw new AppException(HttpStatus.BAD_REQUEST, "Don't have permission to create admin user");

        // add role to user
        user.setRole(role);
    }

    private boolean isAdmin() {
        String cin = authenticationFacade.getAuthentication().getName();
        User user = userRepository.findByCin(cin).orElseThrow(
                () -> new ResourceNotFoundException("User", "cin", cin)
        );
        return user.getRole().getName().equals(ROLES.ROLE_ADMIN.name());
    }

}
