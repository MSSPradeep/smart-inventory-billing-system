package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.security.CustomeUserDetails;
import com.firstproject.smartinventory.service.AppUserDetailsService;
import com.firstproject.smartinventory.service.StoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private StoreServiceImpl storeServiceImpl;

    @PostMapping
    public ResponseEntity<Store> createStore(@RequestBody Store store, Authentication authentication){
        CustomeUserDetails userDetails = (CustomeUserDetails)authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        Store savedStore = storeServiceImpl.createStore(store,currentUser);
        return ResponseEntity.ok(savedStore);
    }

    @GetMapping
    public ResponseEntity<List<Store>> getMyStores(Authentication authentication){
        CustomeUserDetails userDetails = (CustomeUserDetails)authentication.getPrincipal();
        User currentUser = userDetails.getUser();
        return ResponseEntity.ok(storeServiceImpl.getStoresForUser(currentUser));
    }
}
