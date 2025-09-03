package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.mapper.ProductMapper;
import com.firstproject.smartinventory.repository.CategoriesRepository;
import com.firstproject.smartinventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        Product product = ProductMapper.toEntity(productDTO);

        if(productDTO.getCategoryId() != null) {
            Categories category = categoriesRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category Id is not found" + productDTO.getCategoryId()));
            product.setCategories(category);
        }
        Product saved = productRepository.save(product);

        return ProductMapper.toDTO(saved);
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProviderNotFoundException("Product is not Available"+id));

        if(productDTO.getName() != null)
            product.setName(productDTO.getName());
        if(productDTO.getQuantity() >= 1)
            product.setQuantity(productDTO.getQuantity());
        if(productDTO.getBrand() != null)
            product.setBrand(productDTO.getBrand());
        if(productDTO.getPrice() >= 1)
            product.setPrice(productDTO.getPrice());
        if(productDTO.getCategoryId() != null) {
            Categories category = categoriesRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category Id is not found" + productDTO.getCategoryId()));
            product.setCategories(category);
        }
        productRepository.save(product);
        return ProductMapper.toDTO(product);
    }

    @Override
    public void deleteProduct(String id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(temp -> productRepository.delete(temp));
    }
}
