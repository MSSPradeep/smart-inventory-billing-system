package com.firstproject.smartinventory.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.AuthResponseDTO;
import com.firstproject.smartinventory.dto.LoginRequestDTO;
import com.firstproject.smartinventory.dto.RegisterRequestDTO;
import com.firstproject.smartinventory.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private LoginRequestDTO validLogin;
    private RegisterRequestDTO signUp;
    private LoginRequestDTO invalidLogin;

    @BeforeEach
    void setUp(){



        validLogin = new LoginRequestDTO(
                "test1@email.com",
                "testPass"
        );


        invalidLogin = new LoginRequestDTO(
                "testFail1@email.com",
                "testFail"
        );
        
        signUp = new RegisterRequestDTO(
                "test2User",
                "test2@email.com",
                "test2Pass"
        );

    }

    @Test
    void RegisterSuccess() throws Exception {

        MvcResult result = mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUp)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AuthResponseDTO response = objectMapper.readValue(responseBody,AuthResponseDTO.class);

        assertNotNull(response.getToken());

    }

    @Test
    void loginSuccess() throws Exception{


       MvcResult result =  mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLogin)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AuthResponseDTO response = objectMapper.readValue(responseBody, AuthResponseDTO.class);

        assertNotNull(response.getToken());
    }

    @Test
    void registerShouldThrowsException_WhenUserAlreadyExists() throws Exception{

         mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUp)))
                .andExpect(status().isOk())
                .andReturn();

         mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUp)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void loginWithInvalidCredentialsThrowsException() throws Exception{

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidLogin)))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}

