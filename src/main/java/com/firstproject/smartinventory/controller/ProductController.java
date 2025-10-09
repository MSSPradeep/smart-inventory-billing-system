package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.repository.StoreRepository;
import com.firstproject.smartinventory.security.CustomeUserDetails;
import com.firstproject.smartinventory.service.ProductServiceImpl;
import com.firstproject.smartinventory.service.StoreContextService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {


        @Autowired
        private ProductServiceImpl  productServiceImpl;


    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO saved = productServiceImpl.addProduct(productDTO);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productServiceImpl.getAllProducts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable String id, @RequestBody ProductDTO productDTO){
        return ResponseEntity.ok(productServiceImpl.updateProduct(id, productDTO));
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id) {
        productServiceImpl.deleteProduct(id);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id){
        return ResponseEntity.ok(productServiceImpl.getProductById(id));
    }

}
