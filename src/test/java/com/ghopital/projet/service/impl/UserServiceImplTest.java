package com.ghopital.projet.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ghopital.projet.auth.AuthenticationFacade;
import com.ghopital.projet.dto.request.ChangePasswordDto;
import com.ghopital.projet.dto.request.RoleDtoRequest;
import com.ghopital.projet.dto.request.UpdateUserPasswordDto;
import com.ghopital.projet.dto.request.UserDtoRequest;
import com.ghopital.projet.dto.request.UserUpdateDtoRequest;
import com.ghopital.projet.dto.response.RoleDtoResponse;
import com.ghopital.projet.dto.response.UserDtoResponse;
import com.ghopital.projet.entity.File;
import com.ghopital.projet.entity.Role;
import com.ghopital.projet.entity.User;
import com.ghopital.projet.exception.AppException;
import com.ghopital.projet.exception.ResourceNotFoundException;
import com.ghopital.projet.repository.FileRepository;
import com.ghopital.projet.repository.RoleRepository;
import com.ghopital.projet.repository.UserRepository;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {UserServiceImpl.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class UserServiceImplTest {
    @MockBean
    private AuthenticationFacade authenticationFacade;

    @MockBean
    private FileRepository fileRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test:  {@link UserServiceImpl#createUser(UserDtoRequest)}
     */
    @Test
    void testCreateUser() {
        when(userRepository.existsByCin(Mockito.<String>any())).thenReturn(true);
        assertThrows(AppException.class, () -> userServiceImpl.createUser(
                new UserDtoRequest("Jane", "Doe", "Cin", "jane.doe@example.org", "iloveyou", new RoleDtoRequest("Name"))));
        verify(userRepository).existsByCin(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link UserServiceImpl#createUser(UserDtoRequest)}
     */
    @Test
    void testCreateUser2() {
        when(userRepository.existsByCin(Mockito.<String>any())).thenReturn(false);
        when(userRepository.existsByEmail(Mockito.<String>any())).thenReturn(true);
        assertThrows(AppException.class, () -> userServiceImpl.createUser(
                new UserDtoRequest("Jane", "Doe", "Cin", "jane.doe@example.org", "iloveyou", new RoleDtoRequest("Name"))));
        verify(userRepository).existsByCin(Mockito.<String>any());
        verify(userRepository).existsByEmail(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link UserServiceImpl#createUser(UserDtoRequest)}
     */
    @Test
    void testCreateUser3() {
        when(userRepository.existsByCin(Mockito.<String>any()))
                .thenThrow(new ResourceNotFoundException("Role", "Role", "42"));
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.createUser(
                new UserDtoRequest("Jane", "Doe", "Cin", "jane.doe@example.org", "iloveyou", new RoleDtoRequest("Name"))));
        verify(userRepository).existsByCin(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserById(Long)}
     */
    @Test
    void testGetUserById() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        UserDtoResponse actualUserById = userServiceImpl.getUserById(1L);
        verify(userRepository).findById(Mockito.<Long>any());
        assertEquals("Cin", actualUserById.cin());
        assertEquals("Doe", actualUserById.lastName());
        assertEquals("Jane", actualUserById.firstName());
        RoleDtoResponse roleResult = actualUserById.role();
        assertEquals("Name", roleResult.name());
        assertEquals("jane.doe@example.org", actualUserById.email());
        assertNull(roleResult.id());
        assertEquals(1L, actualUserById.id().longValue());
        assertEquals(1L, actualUserById.imageId().longValue());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserById(Long)}
     */
    @Test
    void testGetUserById2() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getUserById(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:  {@link UserServiceImpl#getUserById(Long)}
     */
    @Test
    void testGetUserById3() {
        when(userRepository.findById(Mockito.<Long>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> userServiceImpl.getUserById(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserByCin(String)}
     */
    @Test
    void testGetUserByCin() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByCin(Mockito.<String>any())).thenReturn(ofResult);
        UserDtoResponse actualUserByCin = userServiceImpl.getUserByCin("Cin");
        verify(userRepository).findByCin(Mockito.<String>any());
        assertEquals("Cin", actualUserByCin.cin());
        assertEquals("Doe", actualUserByCin.lastName());
        assertEquals("Jane", actualUserByCin.firstName());
        RoleDtoResponse roleResult = actualUserByCin.role();
        assertEquals("Name", roleResult.name());
        assertEquals("jane.doe@example.org", actualUserByCin.email());
        assertNull(roleResult.id());
        assertEquals(1L, actualUserByCin.id().longValue());
        assertEquals(1L, actualUserByCin.imageId().longValue());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserByCin(String)}
     */
    @Test
    void testGetUserByCin2() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findByCin(Mockito.<String>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getUserByCin("Cin"));
        verify(userRepository).findByCin(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link UserServiceImpl#getUserByCin(String)}
     */
    @Test
    void testGetUserByCin3() {
        when(userRepository.findByCin(Mockito.<String>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> userServiceImpl.getUserByCin("Cin"));
        verify(userRepository).findByCin(Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserByEmail(String)}
     */
    @Test
    void testGetUserByEmail() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        UserDtoResponse actualUserByEmail = userServiceImpl.getUserByEmail("jane.doe@example.org");
        verify(userRepository).findByEmail(Mockito.<String>any());
        assertEquals("Cin", actualUserByEmail.cin());
        assertEquals("Doe", actualUserByEmail.lastName());
        assertEquals("Jane", actualUserByEmail.firstName());
        RoleDtoResponse roleResult = actualUserByEmail.role();
        assertEquals("Name", roleResult.name());
        assertEquals("jane.doe@example.org", actualUserByEmail.email());
        assertNull(roleResult.id());
        assertEquals(1L, actualUserByEmail.id().longValue());
        assertEquals(1L, actualUserByEmail.imageId().longValue());
    }

    /**
     * Method under test: {@link UserServiceImpl#getUserByEmail(String)}
     */
    @Test
    void testGetUserByEmail2() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findByEmail(Mockito.<String>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.getUserByEmail("jane.doe@example.org"));
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link UserServiceImpl#getUserByEmail(String)}
     */
    @Test
    void testGetUserByEmail3() {
        when(userRepository.findByEmail(Mockito.<String>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> userServiceImpl.getUserByEmail("jane.doe@example.org"));
        verify(userRepository).findByEmail(Mockito.<String>any());
    }

    /**
     * Method under test:  {@link UserServiceImpl#getAllUsers()}
     */
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        List<UserDtoResponse> actualAllUsers = userServiceImpl.getAllUsers();
        verify(userRepository).findAll();
        assertTrue(actualAllUsers.isEmpty());
    }

    /**
     * Method under test:  {@link UserServiceImpl#getAllUsers()}
     */
    @Test
    void testGetAllUsers2() {
        when(userRepository.findAll()).thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> userServiceImpl.getAllUsers());
        verify(userRepository).findAll();
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#updateUser(UserUpdateDtoRequest, Long)}
     */
    @Test
    void testUpdateUser() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);

        File image2 = new File();
        image2.setData("AXAXAXAX".getBytes("UTF-8"));
        image2.setId(1L);
        image2.setName("Name");
        image2.setType("Type");

        Role role2 = new Role();
        role2.setName("Name");

        User user2 = new User();
        user2.setCin("Cin");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setImage(image2);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole(role2);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Role role3 = new Role();
        role3.setName("Name");
        Optional<Role> ofResult2 = Optional.of(role3);
        when(roleRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        UserDtoResponse actualUpdateUserResult = userServiceImpl.updateUser(
                new UserUpdateDtoRequest("Jane", "Doe", "Cin", "jane.doe@example.org", new RoleDtoRequest("Name")), 1L);
        verify(roleRepository).findByName(Mockito.<String>any());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
        assertEquals("Cin", actualUpdateUserResult.cin());
        assertEquals("Doe", actualUpdateUserResult.lastName());
        assertEquals("Jane", actualUpdateUserResult.firstName());
        RoleDtoResponse roleResult = actualUpdateUserResult.role();
        assertEquals("Name", roleResult.name());
        assertEquals("jane.doe@example.org", actualUpdateUserResult.email());
        assertNull(roleResult.id());
        assertEquals(1L, actualUpdateUserResult.id().longValue());
        assertEquals(1L, actualUpdateUserResult.imageId().longValue());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#updateUser(UserUpdateDtoRequest, Long)}
     */
    @Test
    void testUpdateUser2() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.save(Mockito.<User>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Role role2 = new Role();
        role2.setName("Name");
        Optional<Role> ofResult2 = Optional.of(role2);
        when(roleRepository.findByName(Mockito.<String>any())).thenReturn(ofResult2);
        assertThrows(AppException.class, () -> userServiceImpl.updateUser(
                new UserUpdateDtoRequest("Jane", "Doe", "Cin", "jane.doe@example.org", new RoleDtoRequest("Name")), 1L));
        verify(roleRepository).findByName(Mockito.<String>any());
        verify(userRepository).findById(Mockito.<Long>any());
        verify(userRepository).save(Mockito.<User>any());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#updatePassword(ChangePasswordDto, Long)}
     */
    @Test
    void testUpdatePassword() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);

        File image2 = new File();
        image2.setData("AXAXAXAX".getBytes("UTF-8"));
        image2.setId(1L);
        image2.setName("Name");
        image2.setType("Type");

        Role role2 = new Role();
        role2.setName("Name");

        User user2 = new User();
        user2.setCin("Cin");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setImage(image2);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole(role2);
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findByCin(Mockito.<String>any())).thenReturn(ofResult2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertThrows(AppException.class,
                () -> userServiceImpl.updatePassword(new ChangePasswordDto("iloveyou", "iloveyou"), 1L));
        verify(authenticationFacade).getAuthentication();
        verify(userRepository).findByCin(Mockito.<String>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#updatePassword(ChangePasswordDto, Long)}
     */
    @Test
    void testUpdatePassword2() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByCin(Mockito.<String>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertThrows(AppException.class,
                () -> userServiceImpl.updatePassword(new ChangePasswordDto("iloveyou", "iloveyou"), 1L));
        verify(authenticationFacade).getAuthentication();
        verify(userRepository).findByCin(Mockito.<String>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#updatePassword(ChangePasswordDto, Long)}
     */
    @Test
    void testUpdatePassword3() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findByCin(Mockito.<String>any())).thenReturn(emptyResult);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertThrows(ResourceNotFoundException.class,
                () -> userServiceImpl.updatePassword(new ChangePasswordDto("iloveyou", "iloveyou"), 1L));
        verify(authenticationFacade).getAuthentication();
        verify(userRepository).findByCin(Mockito.<String>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#updatePassword(String, UpdateUserPasswordDto)}
     */
    @Test
    void testUpdatePassword4() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByCin(Mockito.<String>any())).thenReturn(ofResult);
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertThrows(AppException.class,
                () -> userServiceImpl.updatePassword("Cin", new UpdateUserPasswordDto("iloveyou", "iloveyou", "iloveyou")));
        verify(authenticationFacade).getAuthentication();
        verify(userRepository).findByCin(Mockito.<String>any());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#updatePassword(String, UpdateUserPasswordDto)}
     */
    @Test
    void testUpdatePassword5() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByCin(Mockito.<String>any())).thenReturn(ofResult);
        when(authenticationFacade.getAuthentication())
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class,
                () -> userServiceImpl.updatePassword("Cin", new UpdateUserPasswordDto("iloveyou", "iloveyou", "iloveyou")));
        verify(authenticationFacade).getAuthentication();
        verify(userRepository).findByCin(Mockito.<String>any());
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#updatePassword(String, UpdateUserPasswordDto)}
     */
    @Test
    void testUpdatePassword6() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Principal");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);

        File image2 = new File();
        image2.setData("AXAXAXAX".getBytes("UTF-8"));
        image2.setId(1L);
        image2.setName("Name");
        image2.setType("Type");

        Role role2 = new Role();
        role2.setName("Name");

        User user2 = new User();
        user2.setCin("Cin");
        user2.setEmail("jane.doe@example.org");
        user2.setFirstName("Jane");
        user2.setId(1L);
        user2.setImage(image2);
        user2.setLastName("Doe");
        user2.setPassword("iloveyou");
        user2.setRole(role2);
        when(userRepository.save(Mockito.<User>any())).thenReturn(user2);
        when(userRepository.findByCin(Mockito.<String>any())).thenReturn(ofResult);
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(true);
        String actualUpdatePasswordResult = userServiceImpl.updatePassword("Cin",
                new UpdateUserPasswordDto("iloveyou", "iloveyou", "iloveyou"));
        verify(authenticationFacade).getAuthentication();
        verify(userRepository).findByCin(Mockito.<String>any());
        verify(userRepository).save(Mockito.<User>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
        verify(passwordEncoder).matches(Mockito.<CharSequence>any(), Mockito.<String>any());
        assertEquals("Password changed successfully", actualUpdatePasswordResult);
    }

    /**
     * Method under test:
     * {@link UserServiceImpl#updatePassword(String, UpdateUserPasswordDto)}
     */
    @Test
    void testUpdatePassword7() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Principal");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByCin(Mockito.<String>any())).thenReturn(ofResult);
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        when(passwordEncoder.encode(Mockito.<CharSequence>any()))
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(true);
        assertThrows(AppException.class,
                () -> userServiceImpl.updatePassword("Cin", new UpdateUserPasswordDto("iloveyou", "iloveyou", "iloveyou")));
        verify(authenticationFacade).getAuthentication();
        verify(userRepository).findByCin(Mockito.<String>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
        verify(passwordEncoder).matches(Mockito.<CharSequence>any(), Mockito.<String>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addImageToUser(Long, Long)}
     */
    @Test
    void testAddImageToUser() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(authenticationFacade.getAuthentication())
                .thenReturn(new TestingAuthenticationToken("Principal", "Credentials"));
        assertThrows(AppException.class, () -> userServiceImpl.addImageToUser(1L, 1L));
        verify(authenticationFacade).getAuthentication();
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addImageToUser(Long, Long)}
     */
    @Test
    void testAddImageToUser2() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(authenticationFacade.getAuthentication())
                .thenThrow(new AppException(HttpStatus.CONTINUE, "An error occurred"));
        assertThrows(AppException.class, () -> userServiceImpl.addImageToUser(1L, 1L));
        verify(authenticationFacade).getAuthentication();
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#addImageToUser(Long, Long)}
     */
    @Test
    void testAddImageToUser3() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.addImageToUser(1L, 1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUser(Long)}
     */
    @Test
    void testDeleteUser() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        doNothing().when(userRepository).delete(Mockito.<User>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        userServiceImpl.deleteUser(1L);
        verify(userRepository).delete(Mockito.<User>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUser(Long)}
     */
    @Test
    void testDeleteUser2() throws UnsupportedEncodingException {
        File image = new File();
        image.setData("AXAXAXAX".getBytes("UTF-8"));
        image.setId(1L);
        image.setName("Name");
        image.setType("Type");

        Role role = new Role();
        role.setName("Name");

        User user = new User();
        user.setCin("Cin");
        user.setEmail("jane.doe@example.org");
        user.setFirstName("Jane");
        user.setId(1L);
        user.setImage(image);
        user.setLastName("Doe");
        user.setPassword("iloveyou");
        user.setRole(role);
        Optional<User> ofResult = Optional.of(user);
        doThrow(new AppException(HttpStatus.CONTINUE, "An error occurred")).when(userRepository)
                .delete(Mockito.<User>any());
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(AppException.class, () -> userServiceImpl.deleteUser(1L));
        verify(userRepository).delete(Mockito.<User>any());
        verify(userRepository).findById(Mockito.<Long>any());
    }

    /**
     * Method under test: {@link UserServiceImpl#deleteUser(Long)}
     */
    @Test
    void testDeleteUser3() {
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.deleteUser(1L));
        verify(userRepository).findById(Mockito.<Long>any());
    }
}
