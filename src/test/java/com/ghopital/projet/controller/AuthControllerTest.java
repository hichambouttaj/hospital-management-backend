package com.ghopital.projet.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghopital.projet.auth.UserModel;
import com.ghopital.projet.dto.request.LoginRequestDto;
import com.ghopital.projet.dto.request.UpdateUserPasswordDto;
import com.ghopital.projet.dto.response.RoleDtoResponse;
import com.ghopital.projet.dto.response.UserDtoResponse;
import com.ghopital.projet.entity.Role;
import com.ghopital.projet.service.AuthService;
import com.ghopital.projet.service.UserService;
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

@ContextConfiguration(classes = {AuthController.class})
@ActiveProfiles({"dev"})
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    @Autowired
    private AuthController authController;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserService userService;

    /**
     * Method under test:  {@link AuthController#getCurrentUser(UserModel)}
     */
    @Test
    void testGetCurrentUser() throws Exception {
        when(userService.getUserByCin(Mockito.<String>any())).thenReturn(
                new UserDtoResponse(1L, "Jane", "Doe", "jane.doe@example.org", "Cin", new RoleDtoResponse(1L, "Name"), 1L));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/auth/info");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("userModel",
                String.valueOf(new UserModel("janedoe", "iloveyou", new Role())));
        MockMvcBuilders.standaloneSetup(authController)
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
     * Method under test:  {@link AuthController#getCurrentUser(UserModel)}
     */
    @Test
    void testGetCurrentUser2() throws Exception {
        when(userService.getUserByCin(Mockito.<String>any())).thenReturn(
                new UserDtoResponse(1L, "Jane", "Doe", "jane.doe@example.org", "Cin", new RoleDtoResponse(1L, "Name"), 1L));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/auth/info");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("userModel", String.valueOf(""));
        MockMvcBuilders.standaloneSetup(authController)
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
     * Method under test:  {@link AuthController#loginHandler(LoginRequestDto)}
     */
    @Test
    void testLoginHandler() throws Exception {
        when(authService.login(Mockito.<LoginRequestDto>any())).thenReturn("Login");
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new LoginRequestDto("Cin", "iloveyou")));
        MockMvcBuilders.standaloneSetup(authController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"token\":\"Login\",\"type\":\"Bearer\"}"));
    }

    /**
     * Method under test:
     * {@link AuthController#updatePassword(UserModel, UpdateUserPasswordDto)}
     */
    @Test
    void testUpdatePassword() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post("/api/v1/auth/password");
        MockHttpServletRequestBuilder contentTypeResult = postResult
                .param("userModel", String.valueOf(new UserModel("janedoe", "iloveyou", new Role())))
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new UpdateUserPasswordDto("iloveyou", "iloveyou", "iloveyou")));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(authController).build().perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }
}
