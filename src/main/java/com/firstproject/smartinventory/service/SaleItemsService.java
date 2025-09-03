package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.entity.SaleItems;

import java.util.List;

public interface SaleItemsService {

    SaleItemsResponseDTO getSaleItemById(String id);

    List<SaleItemsResponseDTO> findBySale_SaleId(String SaleId);

    List<SaleItemsResponseDTO> findByProduct_Id(String ProductId);

    Integer getTotalQuantitySoldByProduct(String ProductId);

}
