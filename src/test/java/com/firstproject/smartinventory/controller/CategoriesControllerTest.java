package com.firstproject.smartinventory.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.security.JwtFilter;
import com.firstproject.smartinventory.service.CategoriesServiceImpl;
import lombok.With;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CategoriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoriesServiceImpl categoriesServiceimpl;

    @MockitoBean
    private JwtFilter jwtFilter;

    private CategoriesDTO category1 = new CategoriesDTO(
            "CAT-001",
            "Laptops"
    );
    private CategoriesDTO category2 = new CategoriesDTO(
            "CAT-002",
            "Mobiles"
    );

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategories_success() throws Exception {

        Mockito.when(categoriesServiceimpl.saveCategories(category1)).thenReturn(category1);

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("CAT-001"))
                .andExpect(jsonPath("$.name").value("Laptops"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllCategoriesReturnAllCategories() throws Exception{
        List<CategoriesDTO> categories = List.of(category1,category2);
        Mockito.when(categoriesServiceimpl.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value("CAT-001"))
                .andExpect(jsonPath("$.[0].name").value("Laptops"))
                .andExpect(jsonPath("$.[1].id").value("CAT-002"))
                .andExpect(jsonPath("$.[1].name").value("Mobiles"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategories_success() throws Exception{

        mockMvc.perform(delete("/categories/delete/"+"CAT-002"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategoriesReturnUpdatedCategories() throws Exception{
        CategoriesDTO dto = new CategoriesDTO("CAT-002","Mobile");
        Mockito.when(categoriesServiceimpl.updateCategory("CAT-002",dto)).thenReturn(dto);

        mockMvc.perform(put("/categories/"+"CAT-002")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(jsonPath("$.id").value("CAT-002"))
                .andExpect(jsonPath("$.name").value("Mobile"));
    }
}
