package com.firstproject.smartinventory.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.exception.notFound.ProductNotFoundException;
import com.firstproject.smartinventory.others.IDGenerator;
import com.firstproject.smartinventory.security.JwtFilter;
import com.firstproject.smartinventory.service.ProductServiceImpl;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductServiceImpl productServiceImpl;

    @MockitoBean
    private JwtFilter jwtFilter;

    private final ProductDTO productDTO = new ProductDTO(
            IDGenerator.idGenerator("PROD")
            ,"Laptop"
            ,"ASUS"
            ,56999
            ,1
            ,IDGenerator.idGenerator("CAT")
    );

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldAddProductSuccessfully() throws Exception{

        when(productServiceImpl.addProduct(any(ProductDTO.class))).thenReturn(productDTO);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productDTO.getId()))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(56999));


    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllProducts() throws Exception{

        when(productServiceImpl.getAllProducts()).thenReturn(List.of(productDTO));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].name").value("Laptop"))
                .andExpect(jsonPath("$.[0].brand").value("ASUS"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getProductByIdReturnAProductSuccessfully() throws Exception{

        when(productServiceImpl.getProductById(productDTO.getId())).thenReturn(productDTO);

        mockMvc.perform(get("/products/id/"+productDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.brand").value("ASUS"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getProductsByIdReturnsException() throws Exception{

        String id = "Invalid";

        when(productServiceImpl.getProductById(id)).thenThrow(new ProductNotFoundException("Product is not available with id \""+id+"\""));

        mockMvc.perform(get("/products/id/"+id))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProductSuccessfully() throws Exception{

        String id = productDTO.getId();
        ProductDTO updated = new ProductDTO(id,"VivoBook 15","ASUS",49999,1,productDTO.getCategoryId());
        Mockito.when(productServiceImpl.updateProduct(anyString(),any(ProductDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/products/"+id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("VivoBook 15"))
                .andExpect(jsonPath("$.price").value(49999));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteSavedProduct() throws Exception{

        String id = productDTO.getId();

        mockMvc.perform(delete("/products/"+id))
                .andExpect(status().isOk());
    }

}
