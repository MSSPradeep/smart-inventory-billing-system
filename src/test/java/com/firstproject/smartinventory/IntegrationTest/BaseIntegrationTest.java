package com.firstproject.smartinventory.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.AuthResponseDTO;
import com.firstproject.smartinventory.dto.LoginRequestDTO;
import com.firstproject.smartinventory.dto.RegisterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
@SpringBootTest
@AutoConfigureMockMvc
public class BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public String getJwtToken(String email, String password) throws Exception{
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(email,password);


        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andReturn();

        AuthResponseDTO authResponseDTO = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AuthResponseDTO.class
        );

        return "Bearer "+authResponseDTO.getToken();
    }
}
