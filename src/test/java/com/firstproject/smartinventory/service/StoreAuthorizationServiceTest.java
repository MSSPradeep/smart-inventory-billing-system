package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.exception.auth.UnauthorizedAccessException;
import com.firstproject.smartinventory.exception.badRequest.InvalidInputException;
import com.firstproject.smartinventory.security.SecurityUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StoreAuthorizationServiceTest {

    @Mock
    private SecurityUtil securityUtil;

    @InjectMocks
    private StoreAuthorizationService storeAuthorizationService;

    private Store store;
    private User user;
    private User employee;
    private Set<User> employeelist;

    @BeforeEach
    void setUp() {
        employeelist = new HashSet<>();

        user = new User();
        user.setId("USER-001");

        employee=new User();
        employee.setId("USER-001");
        employee.setUserName("testEmployee");
        employee.setEmail("test1@gmail.com");
        employee.setPassword("test123");
        employeelist.add(employee);

        store = new Store();
        store.setStoreId("STO-001");
        store.setStoreName("test");
        store.setStoreAddress("testAddress");
        store.setOwner(user);
        store.setEmployee(employeelist);

    }

    @Test
    void verifyUserAccess() {
        when(securityUtil.getCurrentUserId()).thenReturn(user.getId());

        storeAuthorizationService.verifyUserAccess(store);

    }

    @Test
    void verifyUserAccess_throwsException_WhenStoreIsNull() {

        InvalidInputException exception = assertThrows(InvalidInputException.class,
                () -> storeAuthorizationService.verifyUserAccess(null));

        assertEquals("Invalid Store Details", exception.getMessage());
    }
    @Test
    void verifyUserAccess_when_whenEmployeeIsNotBelongsToIt(){
        when(securityUtil.getCurrentUserId()).thenReturn("USER-009");

        UnauthorizedAccessException exception = assertThrows(UnauthorizedAccessException.class,
                () ->  storeAuthorizationService.verifyUserAccess(store));

        assertEquals( "Not authorized to access the store", exception.getMessage());
    }

}
