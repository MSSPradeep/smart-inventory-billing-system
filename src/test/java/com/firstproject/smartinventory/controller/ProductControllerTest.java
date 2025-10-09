package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.security.JwtFilter;
import com.firstproject.smartinventory.security.JwtUtil;
import com.firstproject.smartinventory.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private ProductServiceImpl productServiceImpl;

    @WithMockUser(username = "MSSPradeep", roles = {"OWNER"})
    @Test
    void addProduct_ShouldReturnSavedProduct() throws Exception {
        ProductDTO input = new ProductDTO("PROD-001", "Laptop", "ASUS", 56000, 1, "CAT-001");
        ProductDTO saved = new ProductDTO("PROD-001", "Laptop", "ASUS", 56000, 1, "CAT-001");

        when(productServiceImpl.addProduct(Mockito.any(ProductDTO.class))).thenReturn(saved);

        mockMvc.perform(post("/products")
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content("""
                    {
                        "id": "PROD-001",
                        "name": "Laptop",
                        "brand": "ASUS",
                        "price": 56000,
                        "quantity": 1,
                        "categoryId": "CAT-001"
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("PROD-001"))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }
}
