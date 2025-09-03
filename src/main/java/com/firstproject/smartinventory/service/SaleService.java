package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.SaleRequestDTO;
import com.firstproject.smartinventory.dto.SaleResponseDTO;
import com.firstproject.smartinventory.entity.Sale;

import java.util.*;

public interface SaleService {

    public SaleResponseDTO createSale(SaleRequestDTO saleRequestDTO);

    public List<SaleResponseDTO> getAllSales();

    public SaleResponseDTO getSaleById(String saleId);

    List<SaleResponseDTO>  getSalesByDateRange(String startDate, String EndDate);
}
