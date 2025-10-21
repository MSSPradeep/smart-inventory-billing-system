package com.firstproject.smartinventory.IntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.SaleItemsRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SaleItemsControllerIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private SaleItemsRequestDTO requestDTO;
    private String token;

    @BeforeEach
    void setUp(){
        requestDTO = new SaleItemsRequestDTO();
        requestDTO.setProductId("PROD-9E06F3C5");
        requestDTO.setQuantity(1);
    }

    @Test
    void getSaleItemById_success() throws Exception {
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/saleItems/id/SALEITEM-8F7FFC16")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(7))
                .andReturn();
    }

    @Test
    void getSaleItemById_throwsException_whenIdIsInvalid() throws Exception {
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/saleItems/id/invalid")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getSaleItemById_throwsException_whenStaffAccessByStaff() throws Exception {
        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(get("/saleItems/id/SALEITEM-8F7FFC16")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void getSaleItemsBySaleId_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/saleItems/saleId/SAL-79EF32ED")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].totalAmount").value(265000))
                .andReturn();
    }

    @Test
    void getSaleItemBySaleId_throwsException_whenIdIsInvalid() throws Exception {
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/saleItems/SaleId/invalid")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getSaleItemBySaleId_throwsException_whenStaffAccessByStaff() throws Exception {
        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(get("/saleItems/saleId/SALEITEM-8F7FFC16")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void getSaleItemsByProductId_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/saleItems/productId/PROD-9E06F3C5")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].totalAmount").value(265000))
                .andReturn();
    }

    @Test
    void getSaleItemByProductId_throwsException_whenIdIsInvalid() throws Exception {
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/saleItems/productId/invalid")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"))
                .andReturn();
    }

    @Test
    void getSaleItemByProductId_throwsException_whenStaffAccessByStaff() throws Exception {
        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(get("/saleItems/saleId/PROD-9E06F3C5")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
