package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.entity.Categories;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoriesService {

    List<Categories> getAllCategories();
    ResponseEntity<CategoriesDTO> saveCategories();
}
