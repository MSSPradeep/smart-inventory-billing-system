package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.dto.StoreRequestDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.mapper.ProductMapper;
import com.firstproject.smartinventory.repository.CategoriesRepository;
import com.firstproject.smartinventory.repository.ProductRepository;
import com.firstproject.smartinventory.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoriesRepository categoriesRepository;

    @Mock
    private StoreContextService storeContextService;

    @Mock
    private StoreAuthorizationService storeAuthorizationService;

    @InjectMocks
    private ProductServiceImpl productServiceImpl;

    private ProductDTO dto,dto1;
    private Categories categories;
    private Store store;
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
        dto.setId("PROD001");
        dto.setCategoryId("CAT20250901115002006");


        dto1 = new ProductDTO();
        dto1.setName("macbook pro");
        dto1.setPrice(149999.0);
        dto1.setBrand("APPLE");
        dto1.setQuantity(7);
        dto1.setId("PROD002");
        dto1.setCategoryId("CAT20250901115002006");

        store = new Store();
        store.setStoreName("Demo Store");
        store.setStoreAddress("In unit Test");

        product = new Product();
        product.setId("PROD001");
        product.setName("VivoBook 15");
        product.setPrice(55000.0);
        product.setBrand("ASUS");
        product.setQuantity(10);
        product.setCategories(categories);
        product.setStore(store);
    }

    // Testing the addProduct method
    @Test
    void addProduct_ShouldReturnSavedProduct() {
        //Arrange
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(categoriesRepository.findByIdAndStore(categories.getId(), store)).thenReturn(Optional.of(categories));
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
        verify(productRepository,times(1)).save(any(Product.class));
        verify(storeAuthorizationService).verifyUserAccess(store);
    }

    @Test
    void appProduct_whenCategoryNotFound_shouldThrowException(){

        //Arrange
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(categoriesRepository.findByIdAndStore(categories.getId(),store)).thenReturn(Optional.empty());

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, ()->productServiceImpl.addProduct(dto));
        assertEquals("CategoriesId not found "+categories.getId(),exception.getMessage());
        verify(productRepository, never()).save(any(Product.class));
        verify(storeAuthorizationService).verifyUserAccess(store);
    }

    @Test
    void addProduct_whenProductNameIsNull_shouldThrowException(){

        //Arrange
        when(storeContextService.getCurrentStore()).thenReturn(store);
        ProductDTO dto = new ProductDTO();
        dto.setName(null);

        //Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,  () -> productServiceImpl.addProduct(dto));
        assertTrue(exception.getMessage().contains("Product name cannot be null"));
        verify(productRepository,never()).save(any(Product.class));
        verify(storeAuthorizationService).verifyUserAccess(store);
    }

    @Test
    void addProduct_whenStoreIsNull_shouldReturnException(){

        when(storeContextService.getCurrentStore()).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> productServiceImpl.addProduct(dto));
        assertTrue(exception.getMessage().contains("Invalid Store Details"));
        verify(productRepository,never()).save(any(Product.class));
        verify(storeAuthorizationService).verifyUserAccess(isNull());
    }

    //Testing the getAllProducts method
    @Test
    void getAllProducts_ShouldReturnSavedProductsList(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByStore(store)).thenReturn(List.of(product));

        List<ProductDTO> products = productServiceImpl.getAllProducts();

        assertEquals(1, products.size());
        assertEquals("VivoBook 15", products.getFirst().getName());
        verify(storeAuthorizationService).verifyUserAccess(store);
    }

    @Test
    void getAllProducts_WhenInvalidStoreDetails_shouldTrowException(){
        when(storeContextService.getCurrentStore()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->productServiceImpl.getAllProducts());
        assertTrue(exception.getMessage().contains("Invalid Store Details"));

    }

    @Test
    void getAllProducts_emptyList(){

        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByStore(store)).thenReturn(Collections.emptyList());

        List<ProductDTO> products = productServiceImpl.getAllProducts();

        assertTrue(products.isEmpty());
        verify(storeAuthorizationService).verifyUserAccess(store);

    }

    //Testing getProductById method
    @Test
    void getProductById_shouldReturnSavedProduct(){

        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByIdAndStore(product.getId(), store)).thenReturn(Optional.of(product));

        ProductDTO savedProductDTO = productServiceImpl.getProductById("PROD001");

        assertNotNull(savedProductDTO);
        assertEquals(product.getId(), savedProductDTO.getId());
        assertEquals(product.getName(), savedProductDTO.getName());

        verify(productRepository,times(1)).findByIdAndStore(product.getId(),store);
        verify(storeAuthorizationService).verifyUserAccess(store);
    }

    @Test
    void getProductById_shouldThrowException_WhenIdIsNull(){

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> productServiceImpl.getProductById(null));
        assertTrue(exception.getMessage().contains("ID can't be null"));

    }

    @Test
    void getProductById_ShouldThrowException_whenStoreIsnull(){
        when(storeContextService.getCurrentStore()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()-> productServiceImpl.getProductById(product.getId()));
        assertTrue(exception.getMessage().equals("Invalid Store Details"));

    }

    @Test
    void getProductById_shouldThrowException_whenIdIsInvalid(){
        String id = "invalid";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByIdAndStore(id,store))
                .thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()->productServiceImpl.getProductById(id));
        assertEquals("Product is not available with id " + id, exception.getMessage());

        verify(productRepository).findByIdAndStore(id,store);
        verify(storeAuthorizationService).verifyUserAccess(store);

    }

    //Testing updateProduct method
    @Test
    void updateProduct_shouldReturnUpdatedProduct(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(categoriesRepository.findByIdAndStore("CAT20250901115002006",store)).thenReturn(Optional.of(categories));
        when(productRepository.findByIdAndStore("PROD001",store)).thenReturn(Optional.of(product));


        ProductDTO productDTO = productServiceImpl.updateProduct("PROD001", dto1);

        assertEquals("PROD001", productDTO.getId());
        assertEquals(dto1.getName(), productDTO.getName());
        assertEquals(dto1.getCategoryId(),productDTO.getCategoryId());
        verify(productRepository,times(1)).findByIdAndStore("PROD001",store);
        verify(productRepository,times(1)).save(product);
        verify(storeAuthorizationService).verifyUserAccess(store);
    }

    @Test
    void updateProduct_shouldThrowException_whenProductIdIsNotValid(){
        String id = "invalid";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByIdAndStore(id,store)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()->productServiceImpl.updateProduct(id,dto1));

        verify(productRepository,times(0)).save(product);
        verify(storeAuthorizationService).verifyUserAccess(store);
        assertEquals("Product is not Available "+id, exception.getMessage());
    }

    @Test
    void updateProduct_shouldThrowException_whenStoreIsNull(){
        String id = "PROD001";
        Store store1 = null;
        when(storeContextService.getCurrentStore()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()->productServiceImpl.updateProduct(id,dto));
        assertEquals("Store can't be null",exception.getMessage());
        verify(productRepository,times(0)).save(product);
    }

    @Test
    void updateProduct_shouldUpdateSpecificFields_only(){
        String id = "PROD001";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByIdAndStore(id,store)).thenReturn(Optional.of(product));
        when(categoriesRepository.findByIdAndStore(categories.getId(), store)).thenReturn(Optional.of(categories));

        ProductDTO savedDTO = productServiceImpl.updateProduct(id,dto1);

        assertNotEquals(dto1.getId(), savedDTO.getId());
        assertEquals(dto1.getName(),savedDTO.getName());
        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(productRepository,times(1)).save(product);
        verify(categoriesRepository,times(1)).findByIdAndStore(categories.getId(),store);

    }

    //Testing deleteProduct method

    @Test
    void deleteProductShouldDeleteProductById(){

        String id = "PROD001";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByIdAndStore(id,store)).thenReturn(Optional.of(product));

        productServiceImpl.deleteProduct(id);

        verify(productRepository,times(1)).delete(product);
        verify(storeAuthorizationService).verifyUserAccess(store);

    }

    @Test
    void deleteProductShouldThrowException_whenStoreIsInvalid(){
        when(storeContextService.getCurrentStore()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()->productServiceImpl.deleteProduct("PROD001"));

        verify(productRepository,times(0)).delete(product);
    }

    @Test
    void deleteProductShouldThrowException_whenProductIsNotFound(){
        String id = "PROD001";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByIdAndStore(id,store)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows( RuntimeException.class,
                ()-> productServiceImpl.deleteProduct(id));

        assertEquals("Product is not found with id "+id,exception.getMessage());
        verify(productRepository,times(0)).delete(product);

    }

    @Test
    void check_deleteProductIsCalledExactlyOnce(){
        String id = "PROD001";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByIdAndStore(id,store)).thenReturn(Optional.of(product));

        productServiceImpl.deleteProduct(id);

        verify(productRepository,times(1)).delete(product);
        verify(storeAuthorizationService).verifyUserAccess(store);
    }
}
