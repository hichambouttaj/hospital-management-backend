package com.ghopital.projet.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghopital.projet.dto.request.ChangePasswordDto;
import com.ghopital.projet.dto.request.ImageToUserDto;
import com.ghopital.projet.dto.request.RoleDtoRequest;
import com.ghopital.projet.dto.request.UserDtoRequest;
import com.ghopital.projet.dto.request.UserUpdateDtoRequest;
import com.ghopital.projet.dto.response.RoleDtoResponse;
import com.ghopital.projet.dto.response.UserDtoResponse;
import com.ghopital.projet.service.UserService;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {UserController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    /**
     * Method under test:  {@link UserController#updateUser(UserUpdateDtoRequest, long)}
     */
    @Test
    void testUpdateUser2() throws Exception {
        when(userService.updateUser(Mockito.<UserUpdateDtoRequest>any(), Mockito.<Long>any())).thenReturn(
                new UserDtoResponse(1L, "Jane", "Doe", "jane.doe@example.org", "Cin", new RoleDtoResponse(1L, "Name"), 1L));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/api/v1/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper.writeValueAsString(
                new UserUpdateDtoRequest("Jane", "Doe", "U.U.U", "jane.doe@example.org", new RoleDtoRequest("Name"))));
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"cin\":\"Cin\",\"role\":{\"id\":1"
                                        + ",\"name\":\"Name\"},\"imageId\":1}"));
    }

    /**
     * Method under test:  {@link UserController#addImageToUser(Long, ImageToUserDto)}
     */
    @Test
    void testAddImageToUser() throws Exception {
        when(userService.addImageToUser(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(
                new UserDtoResponse(1L, "Jane", "Doe", "jane.doe@example.org", "Cin", new RoleDtoResponse(1L, "Name"), 1L));
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/api/v1/users/{id}/images", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new ImageToUserDto(1L)));
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"cin\":\"Cin\",\"role\":{\"id\":1"
                                        + ",\"name\":\"Name\"},\"imageId\":1}"));
    }

    /**
     * Method under test: {@link UserController#deleteUser(long)}
     */
    @Test
    void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/users/{id}", 1L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("User deleted successfully"));
    }

    /**
     * Method under test: {@link UserController#deleteUser(long)}
     */
    @Test
    void testDeleteUser2() throws Exception {
        doNothing().when(userService).deleteUser(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/v1/users/{id}", 1L);
        requestBuilder.contentType("https://example.org/example");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("User deleted successfully"));
    }

    /**
     * Method under test:  {@link UserController#createUser(UserDtoRequest)}
     */
    @Test
    void testCreateUser() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper.writeValueAsString(
                new UserDtoRequest("Jane", "Doe", "Cin", "jane.doe@example.org", "iloveyou", new RoleDtoRequest("Name"))));
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:  {@link UserController#getAll()}
     */
    @Test
    void testGetAll() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:  {@link UserController#getByCin(String)}
     */
    @Test
    void testGetByCin() throws Exception {
        when(userService.getUserByCin(Mockito.<String>any())).thenReturn(
                new UserDtoResponse(1L, "Jane", "Doe", "jane.doe@example.org", "Cin", new RoleDtoResponse(1L, "Name"), 1L));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users").param("cin", "foo");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"cin\":\"Cin\",\"role\":{\"id\":1"
                                        + ",\"name\":\"Name\"},\"imageId\":1}"));
    }

    /**
     * Method under test:  {@link UserController#getById(Long)}
     */
    @Test
    void testGetById() throws Exception {
        when(userService.getUserById(Mockito.<Long>any())).thenReturn(
                new UserDtoResponse(1L, "Jane", "Doe", "jane.doe@example.org", "Cin", new RoleDtoResponse(1L, "Name"), 1L));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/v1/users/{id}", 1L);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"email\":\"jane.doe@example.org\",\"cin\":\"Cin\",\"role\":{\"id\":1"
                                        + ",\"name\":\"Name\"},\"imageId\":1}"));
    }

    /**
     * Method under test:
     * {@link UserController#updatePassword(ChangePasswordDto, Long)}
     */
    @Test
    void testUpdatePassword() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/api/v1/users/{id}/password", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new ChangePasswordDto("iloveyou", "iloveyou")));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test:
     * {@link UserController#updateUser(UserUpdateDtoRequest, long)}
     */
    @Test
    void testUpdateUser() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.put("/api/v1/users/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult.content(objectMapper.writeValueAsString(
                new UserUpdateDtoRequest("Jane", "Doe", "Cin", "jane.doe@example.org", new RoleDtoRequest("Name"))));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}
