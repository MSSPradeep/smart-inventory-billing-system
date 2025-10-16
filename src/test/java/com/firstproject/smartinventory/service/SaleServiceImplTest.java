package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.SaleItemsRequestDTO;
import com.firstproject.smartinventory.dto.SaleRequestDTO;
import com.firstproject.smartinventory.dto.SaleResponseDTO;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.Sale;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.exception.badRequest.InsufficientStockException;
import com.firstproject.smartinventory.exception.badRequest.InvalidInputException;
import com.firstproject.smartinventory.exception.notFound.ProductNotFoundException;
import com.firstproject.smartinventory.exception.notFound.SaleNotFoundException;
import com.firstproject.smartinventory.repository.ProductRepository;
import com.firstproject.smartinventory.repository.SaleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class
SaleServiceImplTest {

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StoreContextService storeContextService;

    @Mock
    private StoreAuthorizationService storeAuthorizationService;

    @InjectMocks
    private SaleServiceImpl saleServiceImpl;

    private Product product;
    private Sale sale;
    private Store store;
    private SaleRequestDTO saleRequestDTO;
    private SaleItemsRequestDTO saleItemsRequestDTO;

    @BeforeEach
    void setUp(){

        product = new Product();
        product.setName("Laptop");
        product.setBrand("ASUS");
        product.setPrice(51499.0);
        product.setQuantity(10);
        product.setId("PRO20250908");

        sale = new Sale();
        sale.setSaleId("SALE20250908");
        sale.setCustomerName("Unit_Test");
        sale.setDate(LocalDateTime.now());

        store = new Store();
        store.setStoreName("Test_Store");
        store.setStoreAddress("Test_Address");

        saleItemsRequestDTO = new SaleItemsRequestDTO();
        saleItemsRequestDTO.setProductId("PRO20250908");
        saleItemsRequestDTO.setQuantity(7);

        saleRequestDTO = new SaleRequestDTO();
        saleRequestDTO.setCustomerName( "Test_Name");
        saleRequestDTO.setItems(List.of(saleItemsRequestDTO));
    }
    // createSale method
    @Test
    void testCreateSale_Success(){

        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByIdAndStore_StoreId("PRO20250908",store.getStoreId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> {
            Sale sale1 = invocation.getArgument(0);
            sale1.setSaleId("SALE20250908");
            return sale1;
        });

        SaleResponseDTO saleResponseDTO = saleServiceImpl.createSale(saleRequestDTO);

        assertNotNull(saleResponseDTO);
        assertEquals("Test_Name",saleResponseDTO.getCustomerName());
        assertEquals("SALE20250908",saleResponseDTO.getSaleId());
        assertEquals(saleRequestDTO.getItems().getFirst().getQuantity(),saleResponseDTO.getItems().getFirst().getQuantity());
        Optional<Product> product1 = productRepository.findByIdAndStore_StoreId(saleRequestDTO.getItems().getFirst().getProductId(),store.getStoreId());

        product1.ifPresent(value -> assertEquals(value.getPrice() * saleRequestDTO.getItems().getFirst().getQuantity(), saleResponseDTO.getTotalAmount()));


        verify(storeContextService).getCurrentStore();
        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(productRepository).save(any(Product.class));
        verify(saleRepository).save(any(Sale.class));
    }

    @Test
    void testCreateSaleReduceQuantity(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByIdAndStore_StoreId("PRO20250908",store.getStoreId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> {
            Sale sale1 = invocation.getArgument(0);
            return sale1;
        });
        int before_Count = product.getQuantity();

        SaleResponseDTO saleResponseDTO = saleServiceImpl.createSale(saleRequestDTO);

        assertNotNull(saleResponseDTO);
        assertEquals(before_Count-saleRequestDTO.getItems().getFirst().getQuantity(), product.getQuantity());

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(saleRepository,times(1)).save(any(Sale.class));
        verify(productRepository,times(1)).save(any(Product.class));
    }

    @Test
    void testCreateSale_ThrowsExceptionWhen_ProductNotFound(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        String id = "invalid";
        SaleItemsRequestDTO saleItemsRequestDTO1 = new SaleItemsRequestDTO(id,5);
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO("nani",List.of(saleItemsRequestDTO1));

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () ->
                saleServiceImpl.createSale(saleRequestDTO));

        assertEquals("Product not found with ID invalid", exception.getMessage());
        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(saleRepository,times(0)).save(any(Sale.class));
        verify(productRepository,times(0)).save(any(Product.class));    }

    @Test
    void testCreateSale_NotEnoughStock() {

        SaleItemsRequestDTO saleItemsRequestDTO = new SaleItemsRequestDTO("PRO20250908", 12);
        SaleRequestDTO saleRequestDTO = new SaleRequestDTO("nani", List.of(saleItemsRequestDTO));

        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByIdAndStore_StoreId("PRO20250908",store.getStoreId())).thenReturn(Optional.of(product));


        Optional<Product> product1 = productRepository.findByIdAndStore_StoreId("PRO20250908",store.getStoreId());
        InsufficientStockException exception = assertThrows(InsufficientStockException.class, () ->
                saleServiceImpl.createSale(saleRequestDTO));

        product1.ifPresent(value -> assertEquals("Not Enough stock for product "+value.getName(), exception.getMessage()));
        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(saleRepository,times(0)).save(any(Sale.class));
        verify(productRepository,times(0)).save(any(Product.class));
    }

    @Test
    void testCreateSale_ShouldCalculateCorrectly(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(productRepository.findByIdAndStore_StoreId("PRO20250908",store.getStoreId())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> {
            Sale sale1 = invocation.getArgument(0);
            return sale1;
        });
        SaleResponseDTO saleResponseDTO = saleServiceImpl.createSale(saleRequestDTO);

        assertNotNull(saleResponseDTO);
        assertEquals(product.getPrice()*saleRequestDTO.getItems().getFirst().getQuantity(), saleResponseDTO.getTotalAmount());

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(saleRepository,times(1)).save(any(Sale.class));
        verify(productRepository,times(1)).save(any(Product.class));
    }


    @Test
    void testGetAllSales(){

        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(saleRepository.findAllSalesByStore(store)).thenReturn(List.of(sale));

        List<SaleResponseDTO> responseDTOS = saleServiceImpl.getAllSales();

        assertNotNull(responseDTOS);
        assertEquals(1,responseDTOS.size());
        assertEquals("Unit_Test",responseDTOS.getFirst().getCustomerName());

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(saleRepository,times(1)).findAllSalesByStore(store);

    }

    @Test
    void testGetAllSale_EmptyList(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(saleRepository.findAllSalesByStore(store)).thenReturn(Collections.emptyList());

        List<SaleResponseDTO> saleResponse = saleServiceImpl.getAllSales();

        assertTrue(saleResponse.isEmpty());
        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(saleRepository,times(1)).findAllSalesByStore(store);
    }

    @Test
    void getSaleById_Success(){

        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(saleRepository.getSalesBySaleIdAndStore("SALE20250908",store)).thenReturn(sale);

        SaleResponseDTO sale = saleServiceImpl.getSaleById("SALE20250908");

        assertNotNull(sale);
        assertEquals("Unit_Test",sale.getCustomerName());

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(saleRepository,times(1)).getSalesBySaleIdAndStore("SALE20250908",store);

    }

    @Test
    void getSaleBySaleId_NullId(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        InvalidInputException exception = assertThrows(InvalidInputException.class, ()->
            saleServiceImpl.getSaleById(null));

        assertEquals("Entered SaleId is not Valid",exception.getMessage());
        verify(storeAuthorizationService).verifyUserAccess(store);
    }

    @Test
    void getSaleBySaleId_throwExceptionWhen_idIsInvalid(){
        String id = "Invalid";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(saleRepository.getSalesBySaleIdAndStore(id,store)).thenReturn(null);

        SaleNotFoundException exception = assertThrows(SaleNotFoundException.class,
                () -> saleServiceImpl.getSaleById(id));

        assertEquals("Sale not found with ID "+id, exception.getMessage());
    }

    @Test
    void testGetSaleByDateRange_Success() {
        String startDate = LocalDateTime.now().minusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDate = LocalDateTime.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(saleRepository.getSalesByDateRangeAndStore(any(), any(), eq(store))).thenReturn(List.of(sale));

        List<SaleResponseDTO> response = saleServiceImpl.getSalesByDateRange(startDate, endDate);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("Unit_Test", response.getFirst().getCustomerName());

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(saleRepository, times(1)).getSalesByDateRangeAndStore(any(), any(), eq(store));
    }

    @Test
    void testGetSaleByDateRange_EmptyList(){

        String startDate = LocalDateTime.now().minusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String endDate  = LocalDateTime.now().plusDays(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(saleRepository.getSalesByDateRangeAndStore(any(),any(),eq(store))).thenReturn(Collections.emptyList());

        List<SaleResponseDTO> salesList = saleServiceImpl.getSalesByDateRange(startDate,endDate);

        assertEquals(Collections.emptyList(), salesList);

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(saleRepository,times(1)).getSalesByDateRangeAndStore(any(),any(),eq(store));
    }

}
