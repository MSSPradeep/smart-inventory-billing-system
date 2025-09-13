//package com.firstproject.smartinventory.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import com.firstproject.smartinventory.dto.SaleItemsRequestDTO;
//import com.firstproject.smartinventory.dto.SaleRequestDTO;
//import com.firstproject.smartinventory.dto.SaleResponseDTO;
//import com.firstproject.smartinventory.entity.Product;
//import com.firstproject.smartinventory.entity.Sale;
//import com.firstproject.smartinventory.repository.ProductRepository;
//import com.firstproject.smartinventory.repository.SaleRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.aot.AotServices;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class
//SaleServiceImplTest {
//
//    @Mock
//    private SaleRepository saleRepository;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @InjectMocks
//    private SaleServiceImpl saleServiceImpl;
//
//    private Product product;
//    private Sale sale;
//
//    @BeforeEach
//    void setUp(){
//
//        product = new Product();
//        product.setName("Laptop");
//        product.setBrand("ASUS");
//        product.setPrice(51499.0);
//        product.setQuantity(10);
//        product.setId("PRO20250908");
//
//        sale = new Sale();
//        sale.setSaleId("SAL20250908");
//        sale.setCustomerName("Unit_Test");
//        sale.setDate(LocalDateTime.now());
//
//    }
//
//    @Test
//    void testCreateSale_Success(){
//
//        //arrange
//        SaleItemsRequestDTO saleItemsRequestDTO = new SaleItemsRequestDTO("PRO20250908",3);
//        SaleRequestDTO requestDTO = new SaleRequestDTO("nani", List.of(saleItemsRequestDTO));
//
//        when(productRepository.findById("PRO20250908")).thenReturn(Optional.of(product));
//        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation ->{
//            Sale savedSale = (Sale) invocation.getArguments()[0];
//            savedSale.setSaleId("SAL20250908");
//            return savedSale;
//        });
//
//        //Act
//        SaleResponseDTO response = saleServiceImpl.createSale(requestDTO);
//
//        //Assert
//        assertNotNull(response);
//        assertEquals("nani",response.getCustomerName());
//        verify(productRepository,times(1)).save(any(Product.class));
//        verify(saleRepository,times(1)).save(any(Sale.class));
//    }
//
//    @Test
//    void testCreateSale_ProductNotFound(){
//
//        SaleItemsRequestDTO saleItemsRequestDTO = new SaleItemsRequestDTO("invalid",5);
//        SaleRequestDTO saleRequestDTO = new SaleRequestDTO("nani",List.of(saleItemsRequestDTO));
//        when(productRepository.findById("invalid")).thenReturn(Optional.empty());
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () ->
//                saleServiceImpl.createSale(saleRequestDTO));
//
//        assertEquals("Product not found with ID invalid", exception.getMessage());
//    }
//
//    @Test
//    void testCreateSale_NotEnoughStock() {
//
//        SaleItemsRequestDTO saleItemsRequestDTO = new SaleItemsRequestDTO("PRO20250908", 12);
//        SaleRequestDTO saleRequestDTO = new SaleRequestDTO("nani", List.of(saleItemsRequestDTO));
//
//        when(productRepository.findById("PRO20250908")).thenReturn(Optional.of(product));
//
//        RuntimeException exception = assertThrows(RuntimeException.class, () ->
//                saleServiceImpl.createSale(saleRequestDTO));
//
//        assertEquals("Not Enough stock for product Laptop", exception.getMessage());
//    }
//
//    @Test
//    void testGetAllSales(){
//
//        when(saleRepository.findAll()).thenReturn(List.of(sale));
//
//        List<SaleResponseDTO> responseDTOS = saleServiceImpl.getAllSales();
//
//        assertEquals(1,responseDTOS.size());
//        assertEquals("Unit_Test",responseDTOS.getFirst().getCustomerName());
//    }
//
//    @Test
//    void testGetAllSale_EmptyList(){
//        when(saleRepository.findAll()).thenReturn(Collections.emptyList());
//
//        List<SaleResponseDTO> saleResponse = saleServiceImpl.getAllSales();
//
//        assertTrue(saleResponse.isEmpty());
//    }
//
//    @Test
//    void getSaleById_Success(){
//
//        when(saleRepository.getSalesBySaleId("SAL20250908")).thenReturn(sale);
//
//        SaleResponseDTO sale = saleServiceImpl.getSaleById("SAL20250908");
//
//        assertEquals("Unit_Test",sale.getCustomerName());
//        assertNotNull(sale);
//    }
//
//    @Test
//    void getSaleId_NullId(){
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->
//            saleServiceImpl.getSaleById(null));
//
//        assertEquals("Entered SaleId is not Valid",exception.getMessage());
//    }
//
//    @Test
//    void testGetSaleByDateRange(){
//        LocalDateTime now = LocalDateTime.now();
//        when(saleRepository.getSalesByDateRange(any(),any())).thenReturn(List.of(sale));
//
//        List<SaleResponseDTO> responseDTOS = saleServiceImpl.getSalesByDateRange(
//                now.minusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
//                now.plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//        );
//
//        assertEquals(1,responseDTOS.size());
//        assertEquals("Unit_Test",responseDTOS.getFirst().getCustomerName());
//    }
//
//    @Test
//    void testGetSaleByDateRange_EmptyList(){
//        LocalDateTime now = LocalDateTime.now();
//
//        when(saleRepository.getSalesByDateRange(any(),any())).thenReturn(Collections.emptyList());
//
//        List<SaleResponseDTO> response = saleServiceImpl.getSalesByDateRange(
//                now.minusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
//                now.plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//        );
//
//        assertTrue(response.isEmpty());
//    }
//}
