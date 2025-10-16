package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.service.CategoriesServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@CrossOrigin
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private  CategoriesServiceImpl categoriesServiceImpl;



    @GetMapping
    @PreAuthorize("hasRole('OWNER', 'ADMIN','STAFF')")
    public ResponseEntity<List<CategoriesDTO>> getAllCategories(){
        return ResponseEntity.ok(categoriesServiceImpl.getAllCategories());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('OWNER', 'ADMIN')")
    public ResponseEntity<CategoriesDTO> createCategories(@Valid @RequestBody CategoriesDTO categoriesDTO){
         CategoriesDTO saved = categoriesServiceImpl.saveCategories(categoriesDTO);
         return ResponseEntity.ok(saved);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('OWNER', 'ADMIN')")
    public void deleteCategory(@PathVariable String id){
        categoriesServiceImpl.deleteCategory(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER', 'ADMIN')")
    ResponseEntity<CategoriesDTO> updateCategory(@PathVariable String id,@RequestBody CategoriesDTO categoriesDTO){
        return ResponseEntity.ok(categoriesServiceImpl.updateCategory(id,categoriesDTO));
    }
}
