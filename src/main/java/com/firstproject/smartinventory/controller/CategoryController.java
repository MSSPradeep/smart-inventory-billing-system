package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.service.CategoriesServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/categories")
public class CategoryController {

    private final CategoriesServiceImpl categoriesServiceImpl;

    public CategoryController(CategoriesServiceImpl categoriesServiceImpl){
        this.categoriesServiceImpl = categoriesServiceImpl;
    }

    @GetMapping
    public ResponseEntity<List<CategoriesDTO>> getAllCategories(){
        return ResponseEntity.ok(categoriesServiceImpl.getAllCategories());
    }
    
    @PostMapping
    public ResponseEntity<CategoriesDTO> createCategories(@Valid @RequestBody CategoriesDTO categoriesDTO){
         CategoriesDTO saved = categoriesServiceImpl.saveCategories(categoriesDTO);
         return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable String id){
        categoriesServiceImpl.deleteCategory(id);
    }

    @PutMapping("/{id}")
    ResponseEntity<CategoriesDTO> updateCategory(@PathVariable String id,@RequestBody CategoriesDTO categoriesDTO){
        return ResponseEntity.ok(categoriesServiceImpl.updateCategory(id,categoriesDTO));
    }
}
