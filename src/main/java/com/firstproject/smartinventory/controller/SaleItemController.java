package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.entity.SaleItems;
import com.firstproject.smartinventory.service.SaleItemsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
    public SaleItemsResponseDTO getSaleItemById(@PathVariable String id){
        return saleItemsServiceImpl.getSaleItemById(id);
    }
    @GetMapping("/saleId/{SaleId}")
    public List<SaleItemsResponseDTO> findBySale_saleId(@PathVariable String SaleId){
        return saleItemsServiceImpl.findBySale_SaleId(SaleId);
    }
    @GetMapping("/productId/{ProductId}")
    public List<SaleItemsResponseDTO> findByProduct_ProductId(@PathVariable String ProductId){
        return  saleItemsServiceImpl.findByProduct_Id(ProductId);
    }
    @GetMapping("/{ProductId}")
    public Integer getTotalQuantitySoldByProduct(@PathVariable String ProductId){
        return saleItemsServiceImpl.getTotalQuantitySoldByProduct(ProductId);
    }
}

