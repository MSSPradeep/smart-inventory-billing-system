package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.service.SaleItemsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/saleItems")
public class SaleItemController {

    @Autowired
    private SaleItemsServiceImpl saleItemsServiceImpl;

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<SaleItemsResponseDTO> getSaleItemById(@PathVariable String id){
        return ResponseEntity.ok(saleItemsServiceImpl.getSaleItemById(id));
    }
    @GetMapping("/saleId/{SaleId}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<List<SaleItemsResponseDTO>> getSaleItemsBySaleId(@PathVariable String SaleId){
        return ResponseEntity.ok(saleItemsServiceImpl.getSaleItemsBySaleId(SaleId));
    }
    @GetMapping("/productId/{ProductId}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<List<SaleItemsResponseDTO>> getSaleItemsByProductId(@PathVariable String ProductId){
        return  ResponseEntity.ok(saleItemsServiceImpl.getSaleItemsByProductId(ProductId));
    }
    @GetMapping("/{ProductId}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<Integer> getTotalQuantitySoldByProduct(@PathVariable String ProductId){
        return ResponseEntity.ok(saleItemsServiceImpl.getTotalQuantitySoldByProduct(ProductId));
    }
}

