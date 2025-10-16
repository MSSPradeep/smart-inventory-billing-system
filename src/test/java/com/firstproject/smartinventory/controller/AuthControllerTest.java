package com.firstproject.smartinventory.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.AuthResponseDTO;
import com.firstproject.smartinventory.dto.LoginRequestDTO;
import com.firstproject.smartinventory.dto.RegisterRequestDTO;
import com.firstproject.smartinventory.security.JwtFilter;
import com.firstproject.smartinventory.security.JwtUtil;
import com.firstproject.smartinventory.service.AppUserDetailsService;
import com.firstproject.smartinventory.service.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthServiceImpl authServiceImpl;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private AppUserDetailsService appUserDetailsService;

    @MockitoBean
    private JwtUtil jwtUtil;


    private LoginRequestDTO login = new LoginRequestDTO(
            "testemail1@gmail.com",
            "teatPass"
    );

    private AuthResponseDTO authToken = new AuthResponseDTO(
            "JWT_Bearer_Token"
    );

    private RegisterRequestDTO signUp = new RegisterRequestDTO(
            "pradeep",
            "testemail1@gmail.com",
            "test@123"
    );

    @Test
    @WithMockUser(roles = "ADMIN")
    void loginShouldReturnJwtToken() throws Exception {


        Mockito.when(authServiceImpl.login(login)).thenReturn(authToken);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("JWT_Bearer_Token"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void registerShouldReturnJwtToken() throws Exception {

        Mockito.when(authServiceImpl.register(signUp)).thenReturn(authToken);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUp)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("JWT_Bearer_Token"));
    }
}
