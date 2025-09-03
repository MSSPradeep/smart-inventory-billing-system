package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.entity.Sale;
import com.firstproject.smartinventory.entity.SaleItems;
import com.firstproject.smartinventory.mapper.SaleItemMapper;
import com.firstproject.smartinventory.repository.SaleItemsRepository;
import io.jsonwebtoken.lang.Collections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleItemsServiceImpl implements SaleItemsService{

    @Autowired
    private SaleItemsRepository saleItemsRepository;

    @Override
    public SaleItemsResponseDTO getSaleItemById(String id) {
        SaleItems saleItem = saleItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale item is not found with id "+id));

        return SaleItemMapper.toDTO(saleItem);
    }

    @Override
    public List<SaleItemsResponseDTO> findBySale_SaleId(String SaleId) {
        return saleItemsRepository.findBySale_SaleId(SaleId)
                .stream()
                .map(SaleItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SaleItemsResponseDTO> findByProduct_Id(String ProductId) {
        return saleItemsRepository.findByProduct_Id(ProductId)
                .stream()
                .map(SaleItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getTotalQuantitySoldByProduct(String ProductId) {
        return saleItemsRepository.getTotalQuantitySoldByProduct(ProductId);
    }
}
