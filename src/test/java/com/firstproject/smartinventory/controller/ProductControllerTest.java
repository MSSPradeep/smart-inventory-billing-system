package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductServiceImpl productServiceImpl;

    @Test
    void addProduct_ShouldReturnSavedProduct() throws Exception{

        ProductDTO productDTO = new ProductDTO("PRO20250909","VivoBook","ASUS",52499.0,2,)
    }
}
