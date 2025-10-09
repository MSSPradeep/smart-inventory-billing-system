//package com.firstproject.smartinventory.service;
//
//import com.firstproject.smartinventory.dto.CategoriesDTO;
//import com.firstproject.smartinventory.entity.Categories;
//import com.firstproject.smartinventory.entity.Store;
//import com.firstproject.smartinventory.repository.CategoriesRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ssl.SslStoreBundle;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class CategoriesServiceImplTest {
//
//    @Mock
//    private StoreAuthorizationService storeAuthorizationService;
//
//    @Mock
//    private StoreContextService storeContextService;
//
//    @Mock
//    private CategoriesRepository categoriesRepository;
//
//    @InjectMocks
//    private CategoriesServiceImpl categoriesServiceImpl;
//
//    private CategoriesDTO categoriesDTO;
//    private Categories categories;
//    private Store store;
//    private String id = "CAT-001";
//    private String invalidId = "CAT-!00";
//
//    @BeforeEach
//    void setUp(){
//
//        store = new Store();
//        store.setStoreId("STO-001");
//        store.setStoreName("testStore");
//        store.setStoreAddress("testAddress");
//
//        categoriesDTO = new CategoriesDTO();
//        categoriesDTO.setName("Test");
//        categoriesDTO.setId("CAT-001");
//
//        categories = new Categories("CAT-001", "Test",store);
//
//    }
//
//    @Test
//    void getAllCategories_Success(){
//        when(storeContextService.getCurrentStore()).thenReturn(store);
//        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
//        when(categoriesRepository.findByStore(store)).thenReturn(List.of(categories));
//
//        List<CategoriesDTO> out =categoriesServiceImpl.getAllCategories();
//
//        assertNotEquals(null,out);
//        assertEquals(1,out.size());
//        assertEquals(categoriesDTO.getId(), out.getFirst().getId());
//
//        verify(storeContextService,times(1)).getCurrentStore();
//        verify(storeAuthorizationService,times(1)).verifyUserAccess(store);
//        verify(categoriesRepository,times(1)).findByStore(store);
//    }
//
//    @Test
//    void saveCategories_success(){
//        when(storeContextService.getCurrentStore()).thenReturn(store);
//        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
//        when(categoriesRepository.save(sa)).thenReturn(Optional.of(categories));
//
//        CategoriesDTO out = categoriesServiceImpl.saveCategories()
//    }
//}
