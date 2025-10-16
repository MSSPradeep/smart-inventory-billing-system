package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.dto.SaleResponseDTO;
import com.firstproject.smartinventory.entity.SaleItems;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.exception.badRequest.InvalidInputException;
import com.firstproject.smartinventory.exception.notFound.SaleItemNotFoundException;
import com.firstproject.smartinventory.mapper.SaleItemMapper;
import com.firstproject.smartinventory.repository.SaleItemsRepository;
import com.firstproject.smartinventory.repository.SaleRepository;
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
        if (id == null)
            throw new InvalidInputException("Enter valid saleId");
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Store Can't be null");
        storeAuthorizationService.verifyUserAccess(store);
        SaleItems saleItem = saleItemsRepository.findByIdAndStore(id,store);

        if(saleItem == null)
               throw new SaleItemNotFoundException("Sale item is not found with id "+id);

        return SaleItemMapper.toDTO(saleItem);
    }

    @Override
    public List<SaleItemsResponseDTO> getSaleItemsBySaleId(String saleId) {
        if(saleId == null)
            throw new InvalidInputException("SaleItem ID can't be null.");
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Store Can't be null");
        storeAuthorizationService.verifyUserAccess(store);
        return saleItemsRepository.findBySale_SaleIdAndStore(saleId,store)
                .stream()
                .map(SaleItemMapper::toDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<SaleItemsResponseDTO> getSaleItemsByProductId(String productId) {
        if(productId == null)
            throw  new InvalidInputException("Product ID can't be null.");
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Store Can't be null");
        storeAuthorizationService.verifyUserAccess(store);
        return saleItemsRepository.findByProduct_IdAndStore(productId,store)
                .stream()
                .map(SaleItemMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getTotalQuantitySoldByProduct(String productId) {
        if(productId == null)
            throw  new InvalidInputException("Product ID can't be null.");
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Store Can't be null");
        storeAuthorizationService.verifyUserAccess(store);
        Integer total = saleItemsRepository.getTotalQuantitySoldByProductAndStore(productId,store);
        return total != null? total: 0;
    }
}
