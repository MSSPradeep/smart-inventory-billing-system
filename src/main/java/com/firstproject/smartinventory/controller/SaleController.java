package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.SaleRequestDTO;
import com.firstproject.smartinventory.dto.SaleResponseDTO;
import com.firstproject.smartinventory.entity.Sale;
import com.firstproject.smartinventory.service.SaleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private SaleServiceImpl saleServiceImpl;

    @PostMapping
    public SaleResponseDTO createSale(@RequestBody SaleRequestDTO saleRequestDTO){
        return saleServiceImpl.createSale(saleRequestDTO);
    }

    @GetMapping
    public List<SaleResponseDTO> getAllSales(){
        return saleServiceImpl.getAllSales();
    }

    @GetMapping("/id/{id}")
    public SaleResponseDTO getSaleBySaleId( @PathVariable String id){
        return saleServiceImpl.getSaleById(id);
    }

    @GetMapping("/date")
    public List<SaleResponseDTO> getSaleByDateRange(@RequestParam String startDate, @RequestParam String endDate){
        return saleServiceImpl.getSalesByDateRange(startDate,endDate);
    }
}
