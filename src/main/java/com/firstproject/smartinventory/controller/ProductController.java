package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.service.ProductServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductServiceImpl productServiceImpl;

    public ProductController(ProductServiceImpl productServiceImpl){
        this.productServiceImpl = productServiceImpl;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO){
        ProductDTO saved = productServiceImpl.addProduct(productDTO);
        return ResponseEntity.ok(saved);
    }

    @GetMapping
    public List<Product> getAllProducts(){
        return productServiceImpl.getAllProducts();
    }

    @PutMapping("/{id}")
public ProductDTO updateProduct(@PathVariable String id ,@RequestBody ProductDTO productDTO){
       return productServiceImpl.updateProduct(id, productDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable String id){
        productServiceImpl.deleteProduct(id);
    }

}
