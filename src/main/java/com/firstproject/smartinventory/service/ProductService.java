package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.entity.Product;


import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO);

    List<Product> getAllProducts();

    Optional<Product> getProductById(String id);

    ProductDTO updateProduct(String id, ProductDTO productDTO);

    void deleteProduct(String id);
}
