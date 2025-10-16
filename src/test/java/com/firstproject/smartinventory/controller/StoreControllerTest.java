package com.firstproject.smartinventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.StoreRequestDTO;
import com.firstproject.smartinventory.dto.StoreResponseDTO;
import com.firstproject.smartinventory.security.JwtFilter;
import com.firstproject.smartinventory.service.AppUserDetailsService;
import com.firstproject.smartinventory.service.StoreServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(StoreController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StoreServiceImpl storeServiceImpl;

    @MockitoBean
    private AppUserDetailsService appUserDetailsService;

    @MockitoBean
    private JwtFilter jwtFilter;

    private StoreRequestDTO storeRequestDTO = new StoreRequestDTO(
            "testStore",
            "testAdd"
    );

    private StoreResponseDTO storeResponseDTO = new StoreResponseDTO(
            "STORE-001",
            "testStore",
            "testAdd",
            "USER-001",
            "test"
    );
    private StoreResponseDTO storeResponseDTO1 = new StoreResponseDTO(
            "STORE-002",
            "testStore1",
            "testAdd1",
            "USER-001",
            "test1"
    );

    @Test
    @WithMockUser(roles = "ADMIN")
    void createStoreSuccess() throws Exception{

        Mockito.when(storeServiceImpl.createStore(storeRequestDTO)).thenReturn(storeResponseDTO);

        mockMvc.perform(post("/stores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(storeRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value("STORE-001"))
                .andExpect(jsonPath("$.ownerId").value("USER-001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getMyStoresSuccess() throws  Exception{

        List<StoreResponseDTO> stores = List.of(storeResponseDTO,storeResponseDTO1);
        Mockito.when(storeServiceImpl.getStoresForUser()).thenReturn(stores);

        mockMvc.perform(get("/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].storeId").value("STORE-001"))
                .andExpect(jsonPath("$.[0].ownerId").value("USER-001"))
                .andExpect(jsonPath("$.[1].storeId").value("STORE-002"))
                .andExpect(jsonPath("$.[1].ownerId").value("USER-001"));
    }
}
