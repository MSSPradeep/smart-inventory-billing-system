package com.firstproject.smartinventory.IntegrationTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.ProductDTO;
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
public class ProductControllerIntegrationTest extends  BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO validProduct, invalidProduct;
    private String token;
    @BeforeEach
    void setUp (){
        validProduct = new ProductDTO();
        validProduct.setName("macbook");
        validProduct.setBrand("Apple");
        validProduct.setPrice(93000);
        validProduct.setQuantity(20);
        validProduct.setCategoryId("CAT-12158E9D");

        invalidProduct = new ProductDTO();
        invalidProduct.setName("ma");
        invalidProduct.setBrand("le");
        invalidProduct.setPrice(9);
        invalidProduct.setQuantity(0);
        invalidProduct.setCategoryId("CAT-12158E9D");
    }

    @Test
    void addProduct_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");


        mockMvc.perform(post("/products")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validProduct)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void createProduct_throwsException_whenValidationFails() throws Exception {
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(post("/products")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void getAllProducts_success() throws Exception {
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc .perform(get("/products")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Vivo Book 15"))
                .andReturn();
    }


    @Test
    void getProductById_success() throws Exception {
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/products/id/PROD-9E06F3C5")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Vivo Book 15"))
                .andReturn();
    }

    @Test
    void getProductById_throwsException_whenIdIsInvalid() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/products/id/invalid")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void updateProduct_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(put("/products/PROD-9E06F3C5")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validProduct)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("macbook"))
                .andReturn();
    }

    @Test
    void updateProduct_throwsException_whenIdIsInvalid() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(put("/products/invalid")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProduct)))
                .andExpect(status().isNotFound())
                .andReturn();
    }
    @Test
    void updateProduct_throwsException_whenStaffAccessEndPoint() throws Exception{
        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(put("/products/PROD-9E06F3C5")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProduct)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void deleteProduct_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(delete("/products/PROD-9E06F3C5")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void deleteProduct_throwsException_whenIdIsInValid() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(delete("/products/invalid")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
     void deleteProduct_throwsException_whenStaffAccessTheEndPoint() throws Exception{
        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(delete("/products/PROD-9E06F3C5")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
