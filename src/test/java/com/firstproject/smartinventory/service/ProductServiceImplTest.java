package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.repository.CategoriesRepository;
import com.firstproject.smartinventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoriesRepository categoriesRepository;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private ProductDTO dto;
    private Categories categories;
    private Product product;

    @BeforeEach
    void setUp() {
        categories = new Categories();
        categories.setId("CAT20250901115002006");
        categories.setName("Laptop");


        dto = new ProductDTO();
        dto.setName("VivoBook 15");
        dto.setPrice(55000.0);
        dto.setBrand("ASUS");
        dto.setQuantity(10);
        dto.setCategoryId("CAT20250901115002006");

        product = new Product();
        product.setId("PRO20250901115652035");
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());
        product.setCategories(dto.getCategories());

    }

    @Test
    void addProduct_ShouldReturnSavedProduct() {
        //Arrange
        when(categoriesRepository.findById(categories.getId())).thenReturn(Optional.of(categories));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        //Act
        ProductDTO savedProduct = productServiceImpl.addProduct(dto);

        //Assert
        assertNotNull(savedProduct);
        assertNotNull(dto.getName(), savedProduct.getName());
        assertNotNull(dto.getBrand(), savedProduct.getBrand());
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
