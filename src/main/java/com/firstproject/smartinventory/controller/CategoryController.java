package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.service.CategoriesServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/categories")
public class CategoryController {
    HashMap<Integer, Categories> categorysHashMap = new HashMap<>();

    private final CategoriesServiceImpl categoriesServiceImpl;
    public CategoryController(CategoriesServiceImpl categoriesServiceImpl){
        this.categoriesServiceImpl = categoriesServiceImpl;
    }

    @GetMapping
    public List<Categories> getAllCategories(){
        return categoriesServiceImpl.getAllCategories();
    }
    
    @PostMapping
    public ResponseEntity<CategoriesDTO> createCategories(@Valid @RequestBody CategoriesDTO categoriesDTO){
         CategoriesDTO saved = categoriesServiceImpl.saveCategories(categoriesDTO);
         return ResponseEntity.ok(saved);
    }
}
