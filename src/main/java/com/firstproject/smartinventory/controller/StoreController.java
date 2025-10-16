package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.StoreRequestDTO;
import com.firstproject.smartinventory.dto.StoreResponseDTO;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.security.CustomeUserDetails;
import com.firstproject.smartinventory.service.AppUserDetailsService;
import com.firstproject.smartinventory.service.StoreServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/stores")
public class StoreController {

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private StoreServiceImpl storeServiceImpl;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<StoreResponseDTO> createStore(@RequestBody StoreRequestDTO storeRequestDTO){
        StoreResponseDTO savedStore = storeServiceImpl.createStore(storeRequestDTO);
        return ResponseEntity.ok(savedStore);
    }

    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<List<StoreResponseDTO>> getMyStores(){

        return ResponseEntity.ok(storeServiceImpl.getStoresForUser());
    }
}
