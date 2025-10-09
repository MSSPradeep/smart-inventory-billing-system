package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.SaleItemsRequestDTO;
import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.dto.SaleResponseDTO;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.Sale;
import com.firstproject.smartinventory.entity.SaleItems;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.mapper.SaleItemMapper;
import com.firstproject.smartinventory.repository.SaleItemsRepository;
import com.firstproject.smartinventory.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import  static com.firstproject.smartinventory.others.IDGenerator.idGenerator;

@ExtendWith(MockitoExtension.class)
public class SaleItemsServiceImplTest {

    @Mock
    private StoreAuthorizationService storeAuthorizationService;

    @Mock
    private StoreContextService storeContextService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private SaleItemsRepository saleItemsRepository;

    @InjectMocks
    private SaleItemsServiceImpl saleItemsServiceImpl;

    private Store store;
    private Product product;
    private SaleItems saleItems;
    private SaleItemsResponseDTO saleItemsResponseDTO;

    @BeforeEach
    void setUp(){
        store = new Store();
        store.setStoreId("test-id");

        product = new Product();
        product.setName("ASUS VivoBook 15");
        product.setQuantity(7);
        product.setPrice(53999.0);
        product.setId("PROD20250921");

        saleItems = new SaleItems();
        saleItems.setId("item-123");
        saleItems.setProduct(product);
        saleItems.setQuantity(2);
        saleItems.setPrice(product.getPrice());
        saleItems.setSubTotal(saleItems.getQuantity() * saleItems.getPrice());

        saleItemsResponseDTO = new SaleItemsResponseDTO();
        saleItemsResponseDTO.setPrice(saleItems.getPrice());
        saleItemsResponseDTO.setProductName(saleItems.getProduct().getName());
        saleItemsResponseDTO.setQuantity(saleItems.getQuantity());
        saleItemsResponseDTO.setTotalAmount(saleItems.getSubTotal());
    }

    @Test
    void getSaleItemsById_success(){
        String id = "item-123";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(saleItemsRepository.findByIdAndStore(id,store)).thenReturn(saleItems);

        SaleItemsResponseDTO response = saleItemsServiceImpl.getSaleItemById(id);

        assertNotNull(response);
        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(saleItemsRepository,times(1)).findByIdAndStore(id,store);
    }

    @Test
    void getSaleItemsById_throwException_whenIdIsNull(){

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,

                () -> saleItemsServiceImpl.getSaleItemById(null));

        assertEquals("Enter valid saleId", exception.getMessage());
    }

    @Test
    void getSaleItemsById_throwsException_whenSaleItemIsNull(){
        String id = "item-001";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(saleItemsRepository.findByIdAndStore(id,store)).thenReturn(null);

        RuntimeException exception = assertThrows( RuntimeException.class,
                () -> saleItemsServiceImpl.getSaleItemById(id));

        assertEquals("Sale item is not found with id "+id,exception.getMessage());

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(saleItemsRepository,times(1)).findByIdAndStore(id,store);
    }

    @Test
    void getSaleItemsBySaleId_success(){
        String id = "item-123";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(saleItemsRepository.findBySale_SaleIdAndStore(id,store)).thenReturn(List.of(saleItems));

        List<SaleItemsResponseDTO> response = saleItemsServiceImpl.getSaleItemsBySaleId(id);

        assertNotNull(response);
        assertEquals(1,response.size());
        assertEquals("ASUS VivoBook 15",response.getFirst().getProductName());
        assertEquals("item-123",response.getFirst().getId());

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(storeContextService,times(1)).getCurrentStore();
        verify(saleItemsRepository,times(1)).findBySale_SaleIdAndStore(id,store);
    }

    @Test
    void getSaleItemsBySaleId_throwException_WhenIdIsNull(){


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saleItemsServiceImpl.getSaleItemsBySaleId(null));

        assertEquals("SaleItem ID can't be null.",exception.getMessage());
    }

    @Test
    void getSaleItemsBySaleId_shouldReturn_EmptyList(){
        String id = "item-001";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(saleItemsRepository.findBySale_SaleIdAndStore(id,store)).thenReturn(Collections.emptyList());

        List<SaleItemsResponseDTO> saved = saleItemsServiceImpl.getSaleItemsBySaleId(id);

        assertEquals(Collections.emptyList(),saved);
        assertEquals(0,saved.size());
    }

    @Test
    void getSaleItemsByProductId_success(){
        String id = "PROD20250921";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(saleItemsRepository.findByProduct_IdAndStore(id,store)).thenReturn(List.of(saleItems));

        List<SaleItemsResponseDTO> saved = saleItemsServiceImpl.getSaleItemsByProductId(id);

        assertEquals(1, saved.size());
        assertEquals("ASUS VivoBook 15",saved.getFirst().getProductName());

        verify(storeContextService,times(1)).getCurrentStore();
        verify(storeAuthorizationService,times(1)).verifyUserAccess(store);
        verify(saleItemsRepository,times(1)).findByProduct_IdAndStore(id,store);
    }

    @Test
    void getSaleItemsByProductId_throwsException_whenIdIsNull(){

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saleItemsServiceImpl.getSaleItemsByProductId(null));

        assertEquals("Product ID can't be null.",exception.getMessage());

        verify(saleItemsRepository,times(0)).findByProduct_IdAndStore(null,store);
    }

    @Test
    void getSaleItemsByProductId_shouldReturn_EmptyList(){
        String id = "PROD20250923";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(saleItemsRepository.findByProduct_IdAndStore(id,store)).thenReturn(Collections.emptyList());

        List<SaleItemsResponseDTO> saved = saleItemsServiceImpl.getSaleItemsByProductId(id);

        assertEquals(Collections.emptyList(),saved);
        assertEquals(0, saved.size());

        verify(storeContextService,times(1)).getCurrentStore();
        verify(storeAuthorizationService,times(1)).verifyUserAccess(store);
        verify(saleItemsRepository,times(1)).findByProduct_IdAndStore(id,store);
    }

    @Test
    void getTotalQuantitySoldByProduct(){
        String id = "PROD20250921";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(saleItemsRepository.getTotalQuantitySoldByProductAndStore(id,store)).thenReturn(1);

        Integer out = saleItemsServiceImpl.getTotalQuantitySoldByProduct(id);

        assertEquals(1,out);

        verify(storeAuthorizationService,times(1)).verifyUserAccess(store);
        verify(storeContextService,times(1)).getCurrentStore();
        verify(saleItemsRepository,times(1)).getTotalQuantitySoldByProductAndStore(id,store);

    }

    @Test
    void getTotalQuantitySoldByProduct_throwsException_whenIdIsNull(){

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> saleItemsServiceImpl.getTotalQuantitySoldByProduct(null));


        assertEquals("Product ID can't be null.",exception.getMessage());
        verify(saleItemsRepository,times(0)).getTotalQuantitySoldByProductAndStore(null,store);
    }

}
