package com.firstproject.smartinventory.IntegrationTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.SaleItemsRequestDTO;
import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.dto.SaleRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SaleControllerIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private SaleRequestDTO saleRequestDTO;
    private SaleItemsRequestDTO item1, item2;
    private String token;
    List<SaleItemsRequestDTO> saleItems;
    @BeforeEach
    void setUp(){
        item1 = new SaleItemsRequestDTO();
        item1.setProductId("PROD-9E06F3C5");
        item1.setQuantity(5);

        item2 = new SaleItemsRequestDTO();
        item2.setProductId("PROD-9E06F3C5");
        item2.setQuantity(7);

        saleItems = List.of(item1,item2);

        saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setCustomerName("testUser");
        saleRequestDTO.setItems(saleItems);
    }

    @Test
    void createStore_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(post("/sales")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saleRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productName").value("Vivo Book 15"))
                .andReturn();
    }

    @Test
    void createStore_throwsException_whenValidationFails() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(post("/sales")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new SaleRequestDTO("ne",saleItems))))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void getAllSales_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/sales")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].items[0].productName").value("Vivo Book 15"))
                .andReturn();
    }

    @Test
    void getAllSales_throwsException_whenStaffAccessEndPoint() throws Exception {
        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

            mockMvc.perform(get("/sales")
                    .header("Authorization",token)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden())
                    .andReturn();
    }

    @Test
    void getSaleBySaleId_success() throws Exception{

        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/sales/id/SAL-79EF32ED")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(636000))
                .andReturn();
    }

    @Test
    void getSaleBySaleId_throwsException() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/sales/id/invalid")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getSalesByDateRange_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/sales/date")
                .header("Authorization",token)
                .param("startDate","2025-10-20 00:00:00")
                .param("endDate","2025-10-22 23:59:59")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].saleId").value("SAL-79EF32ED"))
                .andReturn();
    }

    @Test
    void getSalesByDateRange_throwsException_whenStaffAccessEndPoint() throws Exception{
        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(get("/sales/date")
                        .header("Authorization",token)
                        .param("startDate","2025-10-20 00:00:00")
                        .param("endDate","2025-10-22 23:59:59")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
