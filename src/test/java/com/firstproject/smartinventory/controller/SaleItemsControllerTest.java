package com.firstproject.smartinventory.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.SaleItemsRequestDTO;
import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.security.JwtFilter;
import com.firstproject.smartinventory.service.SaleItemsServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(SaleItemController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SaleItemsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SaleItemsServiceImpl saleItemsServiceImpl;

    @MockitoBean
    private JwtFilter jwtFilter;

    private SaleItemsRequestDTO saleItemsRequestDTO = new SaleItemsRequestDTO(
            "PROD-001",
            3
    );

    private SaleItemsResponseDTO saleItemsResponseDTO1 = new SaleItemsResponseDTO(
            "SALEITEM-001",
            "PROD-001",
            "VivoBook 15",
            2,
            49999,
            99999
    );

    private SaleItemsResponseDTO saleItemsResponseDTO2 = new SaleItemsResponseDTO(
            "SALEITEM-002",
            "PROD-002",
            "VivoBook 15 pro",
            1,
            69999,
            69999
    );
    private SaleItemsResponseDTO saleItemsResponseDTO3 = new SaleItemsResponseDTO(
            "SALEITEM-001",
            "PROD-001",
            "VivoBook 15",
            10,
            49999,
            499990
    );


    @Test
    @WithMockUser(roles = "ADMIN")
    void getSaleItemsById_success() throws Exception{

        String id = "SALEITEM-001";
        Mockito.when(saleItemsServiceImpl.getSaleItemById(id)).thenReturn(saleItemsResponseDTO1);

        mockMvc.perform(get("/saleItems/id/"+id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value("PROD-001"))
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSaleItemsBySaleID() throws Exception {
        String saleId = "SALE-001";
        List<SaleItemsResponseDTO> items = List.of(saleItemsResponseDTO1,saleItemsResponseDTO2);
        Mockito.when(saleItemsServiceImpl.getSaleItemsBySaleId(saleId)).thenReturn(items);

        mockMvc.perform(get("/saleItems/saleId/"+saleId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value("SALEITEM-001"))
                .andExpect(jsonPath("$.[0].productName").value("VivoBook 15"))
                .andExpect(jsonPath("$.[1].id").value("SALEITEM-002"))
                .andExpect(jsonPath("$.[1].productName").value("VivoBook 15 pro"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getSaleItemsByProductID() throws Exception{
        String id  = saleItemsResponseDTO1.getProductId();
        List<SaleItemsResponseDTO> items = List.of(saleItemsResponseDTO3,saleItemsResponseDTO1);
        Mockito.when(saleItemsServiceImpl.getSaleItemsByProductId(id)).thenReturn(items);

        mockMvc.perform(get("/saleItems/productId/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].productName").value("VivoBook 15"))
                .andExpect(jsonPath("$.[0].quantity").value(10))
                .andExpect(jsonPath("$.[1].productName").value("VivoBook 15"))
                .andExpect(jsonPath("$.[1].quantity").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTotalQuantitySoldByProductId_success() throws Exception {
        String id = saleItemsResponseDTO1.getProductId();
        Integer quantity = 12;

        Mockito.when(saleItemsServiceImpl.getTotalQuantitySoldByProduct(id)).thenReturn(quantity);

        mockMvc.perform(get("/saleItems/"+id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(quantity.toString()));

    }
}
