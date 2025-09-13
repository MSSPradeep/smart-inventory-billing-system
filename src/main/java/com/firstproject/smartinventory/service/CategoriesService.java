package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.entity.Store;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoriesService {

    List<Categories> getAllCategories();
    CategoriesDTO saveCategories(CategoriesDTO categoriesDTO);
    CategoriesDTO updateCategory(String id, CategoriesDTO categoriesDTO);
    void deleteCategory(String id);
}
