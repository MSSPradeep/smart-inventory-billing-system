package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;

import java.util.List;

public interface SaleItemsService {

    SaleItemsResponseDTO getSaleItemById(String id);

    List<SaleItemsResponseDTO> getSaleItemsBySaleId(String SaleId);

    List<SaleItemsResponseDTO> getSaleItemsByProductId(String ProductId);

    Integer getTotalQuantitySoldByProduct(String ProductId);

}
