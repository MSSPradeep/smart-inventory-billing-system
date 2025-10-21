package com.firstproject.smartinventory.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.StoreRequestDTO;
import com.firstproject.smartinventory.dto.StoreResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class StoreControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private StoreResponseDTO response;
    private StoreRequestDTO validStore1,validStore2, invalidStore;

    private String token;

    @BeforeEach
    void setUp() {

        validStore1 = new StoreRequestDTO(
                "test1 store",
                "test1 address"
        );

        validStore2 = new StoreRequestDTO(
                "test2 store",
                "test2 address"
        );
        invalidStore = new StoreRequestDTO(
                "",
                "test address"
        );

    }

    @Test
    void createStore_success() throws Exception {
        String token = super.getJwtToken("test1@email.com", "testPass");

        mockMvc.perform(post("/stores")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore1)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void createStore_throwsException_whenValidationFails() throws Exception {
        token = super.getJwtToken("test1@email.com", "testPass");

        mockMvc.perform(post("/stores")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStore)))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    void createStore_throwsException_whenAdminTryTOCreateStore() throws Exception {

        token = super.getJwtToken("testAdmin1@email.com", "testAdminPass");

        mockMvc.perform(post("/stores")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore1)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void createStore_throwsException_whenStaffTryTOCreateStore() throws Exception {

        token = super.getJwtToken("testStaff1@email.com", "testStaffPass");

        mockMvc.perform(post("/stores")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore1)))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    void getMyStores_success() throws Exception{
         token = super.getJwtToken("test1@email.com","testPass");

         mockMvc.perform(post("/stores")
                 .header("Authorization",token)
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(validStore1)))
                         .andExpect(status().isOk())
                                 .andReturn();
        mockMvc.perform(post("/stores")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore2)))
                .andExpect(status().isOk())
                .andReturn();

         mockMvc.perform(get("/stores")
                 .header("Authorization",token)
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.[*].storeName").value(containsInAnyOrder("test1 store","test2 store","testStore")))
                 .andReturn();
    }

    @Test
    void getMyStores_throwsException_whenAdminAccessThisEndPoint() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        String admin = super.getJwtToken("testAdmin1@email.com","testAdminPass");

        mockMvc.perform(post("/stores")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore1)))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(post("/stores")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore2)))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/stores")
                        .header("Authorization",admin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void getMyStores_throwsException_whenStaffAccessThisEndPoint() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        String staff = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(post("/stores")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore1)))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(post("/stores")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validStore2)))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/stores")
                        .header("Authorization",staff)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
