package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.SaleRequestDTO;
import com.firstproject.smartinventory.dto.SaleResponseDTO;
import com.firstproject.smartinventory.entity.Sale;
import com.firstproject.smartinventory.service.SaleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleServiceImpl saleServiceImpl;

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN','STAFF')")
    public ResponseEntity<SaleResponseDTO> createSale(@Valid @RequestBody SaleRequestDTO saleRequestDTO){
        return ResponseEntity.ok(saleServiceImpl.createSale(saleRequestDTO));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<List<SaleResponseDTO> >getAllSales(){
        return ResponseEntity.ok(saleServiceImpl.getAllSales());
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN','STAFF')")
    public ResponseEntity<SaleResponseDTO> getSaleBySaleId( @PathVariable String id){
        return ResponseEntity.ok(saleServiceImpl.getSaleById(id));
    }

    @GetMapping("/date")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    public ResponseEntity<List<SaleResponseDTO>> getSaleByDateRange(@RequestParam String startDate, @RequestParam String endDate){
        return ResponseEntity.ok(saleServiceImpl.getSalesByDateRange(startDate,endDate));
    }
}
