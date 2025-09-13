package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.repository.StoreRepository;
import com.firstproject.smartinventory.security.CustomeUserDetails;
import com.firstproject.smartinventory.service.CategoriesServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/categories")
public class CategoryController {

    private final CategoriesServiceImpl categoriesServiceImpl;

    public CategoryController(CategoriesServiceImpl categoriesServiceImpl){
        this.categoriesServiceImpl = categoriesServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<Categories>> getAllCategories(){
        return ResponseEntity.ok(categoriesServiceImpl.getAllCategories());
    }
    
    @PostMapping
    public ResponseEntity<CategoriesDTO> createCategories(@Valid @RequestBody CategoriesDTO categoriesDTO){
         CategoriesDTO saved = categoriesServiceImpl.saveCategories(categoriesDTO);
         return ResponseEntity.ok(saved);
    }
    @DeleteMapping
    public void deleteCategory(String id){
        categoriesServiceImpl.deleteCategory(id);
    }

    @PutMapping
    ResponseEntity<CategoriesDTO> updateCategory(String id, CategoriesDTO categoriesDTO){
        return ResponseEntity.ok(categoriesServiceImpl.updateCategory(id,categoriesDTO));
    }
}
