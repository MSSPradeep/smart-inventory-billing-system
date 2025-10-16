package com.firstproject.smartinventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.SaleItemsRequestDTO;
import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.dto.SaleRequestDTO;
import com.firstproject.smartinventory.dto.SaleResponseDTO;
import com.firstproject.smartinventory.others.IDGenerator;
import com.firstproject.smartinventory.security.JwtFilter;
import com.firstproject.smartinventory.service.SaleServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(SaleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SaleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SaleServiceImpl saleServiceImpl;

    @MockitoBean
    private JwtFilter jwtFilter;

    private final LocalDateTime date = LocalDateTime.now();

    private  SaleItemsRequestDTO saleItemsRequestDTO = new SaleItemsRequestDTO("PROD-001",3);

    private SaleItemsResponseDTO saleItemsResponseDTO = new SaleItemsResponseDTO(
            IDGenerator.idGenerator("STORE")
            ,"PROD-001"
            ,"VivoBook 15"
            ,3
            ,49999
            ,14997.0);

    private  SaleRequestDTO saleRequestDTO = new SaleRequestDTO("user", List.of(saleItemsRequestDTO));

    private SaleResponseDTO saleResponseDTO = new SaleResponseDTO(
            IDGenerator.idGenerator("SALE"),
             date,
            saleItemsResponseDTO.getTotalAmount(),
            "user",
            List.of(saleItemsResponseDTO)
    );

    @Test
    @WithMockUser(roles = "ADMIN")
    void testCreateSale_Success() throws  Exception{

        when(saleServiceImpl.createSale(any(SaleRequestDTO.class))).thenReturn(saleResponseDTO);

        mockMvc.perform(post("/sales").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saleRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("user"))
                .andExpect(jsonPath("$.totalAmount").value(14997.0))
                .andExpect(jsonPath("$.items[0].productId").value("PROD-001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetAllSales_Success() throws Exception{

        String id = saleResponseDTO.getSaleId();
        when(saleServiceImpl.getAllSales()).thenReturn(List.of(saleResponseDTO));


        mockMvc.perform(get("/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].customerName").value("user"))
                .andExpect(jsonPath("$.[0].totalAmount").value(14997.0))
                .andExpect(jsonPath("$.[0].items[0].productId").value("PROD-001"));

    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetSaleById_success() throws Exception{
        String id = saleResponseDTO.getSaleId();
        Mockito.when(saleServiceImpl.getSaleById(id )).thenReturn(saleResponseDTO);

        mockMvc.perform(get("/sales/id/"+id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("user"))
                .andExpect(jsonPath("$.totalAmount").value(14997.0))
                .andExpect(jsonPath("$.items[0].productId").value("PROD-001"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetSaleByDateRange_success() throws Exception{
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String start ="2025-01-16 00:00:00";
        String end = "2025-10-16 23:59:59";
        Mockito.when(saleServiceImpl.getSalesByDateRange(start,end)).thenReturn(List.of(saleResponseDTO));

        mockMvc.perform(get("/sales/date")
                        .param("startDate",start)
                        .param("endDate",end)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].customerName").value("user"))
                .andExpect(jsonPath("$.[0].totalAmount").value(14997.0))
                .andExpect(jsonPath("$.[0].items[0].productId").value("PROD-001"));
    }
}
