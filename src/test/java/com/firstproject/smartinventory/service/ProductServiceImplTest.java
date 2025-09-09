package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.repository.CategoriesRepository;
import com.firstproject.smartinventory.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

    }

    @Test
    void addProduct_ShouldReturnSavedProduct() {
        //Arrange
        when(categoriesRepository.findById(categories.getId())).thenReturn(Optional.of(categories));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArguments()[0]);

        //Act
        ProductDTO savedProduct = productServiceImpl.addProduct(dto);

        //Capture the entity passed to save
        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product savedEntity = captor.getValue();

        //Assert DTO returned
        assertNotNull(savedProduct);
        assertNotNull(dto.getName(), savedProduct.getName());

        //Assert saved Entity field
        assertEquals(dto.getName(),savedEntity.getName());
        assertEquals(dto.getBrand(),savedEntity.getBrand());
        assertEquals(dto.getPrice(),savedEntity.getPrice());
        assertEquals(dto.getQuantity(),savedEntity.getQuantity());
        assertEquals(categories,savedEntity.getCategories());
    }

    @Test
    void appProduct_whenCategoryNotFound_shouldThrowException(){

        //Arrange
        when(categoriesRepository.findById(categories.getId())).thenReturn(Optional.empty());

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, ()->productServiceImpl.addProduct(dto));
        assertEquals("CategoriesId not found "+categories.getId(),exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void addProduct_whenProductNameIsNull_shouldThrowException(){

        //Arrange
        ProductDTO dto = new ProductDTO();
        dto.setName(null);

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,  () -> productServiceImpl.addProduct(dto));
        assertTrue(exception.getMessage().contains("Product name cannot be null"));
        verify(productRepository,never()).save(any(Product.class));

    }
}
