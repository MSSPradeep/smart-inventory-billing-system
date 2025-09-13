package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.entity.SaleItems;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.mapper.SaleItemMapper;
import com.firstproject.smartinventory.repository.SaleItemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleItemsServiceImpl implements SaleItemsService{

    @Autowired
    private SaleItemsRepository saleItemsRepository;

    @Autowired
    private StoreContextService storeContextService;

    @Autowired
    private StoreAuthorizationService storeAuthorizationService;

    @Override
    public SaleItemsResponseDTO getSaleItemById(String id) {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        SaleItems saleItem = saleItemsRepository.findByIdAndStore(id,store);

        if(saleItem == null)
               throw new RuntimeException("Sale item is not found with id "+id);

        return SaleItemMapper.toDTO(saleItem);
    }

    @Override
    public List<SaleItemsResponseDTO> findBySale_SaleId(String SaleId) {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        return saleItemsRepository.findBySale_SaleIdAndStore(SaleId,store)
                .stream()
                .map(SaleItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SaleItemsResponseDTO> findByProduct_Id(String ProductId) {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        return saleItemsRepository.findByProduct_IdAndStore(ProductId,store)
                .stream()
                .map(SaleItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getTotalQuantitySoldByProduct(String ProductId) {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        Integer total = saleItemsRepository.getTotalQuantitySoldByProductAndStore(ProductId,store);
        return total != null? total: 0;
    }
}
