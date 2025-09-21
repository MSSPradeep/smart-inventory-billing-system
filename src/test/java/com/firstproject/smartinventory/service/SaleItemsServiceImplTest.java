package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.entity.Sale;
import com.firstproject.smartinventory.entity.SaleItems;
import com.firstproject.smartinventory.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.IdGenerator;

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


    @InjectMocks
    private StoreServiceImpl storeServiceImpl;


    private SaleItems saleItems;
    private Sale sale;

    @BeforeEach
    void setUp(){
        sale = new Sale();
        sale.setSaleId("SALE");

        saleItems = new SaleItems();
        saleItems.setSale();
    }
}
