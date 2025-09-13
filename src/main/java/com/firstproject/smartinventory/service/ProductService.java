package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.Store;


import java.util.List;
import java.util.Optional;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO);

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(String id);

    ProductDTO updateProduct(String id, ProductDTO productDTO);

    void deleteProduct(String id);
}
