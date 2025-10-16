package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.*;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.Sale;
import com.firstproject.smartinventory.entity.SaleItems;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.exception.badRequest.InsufficientStockException;
import com.firstproject.smartinventory.exception.badRequest.InvalidInputException;
import com.firstproject.smartinventory.exception.notFound.ProductNotFoundException;
import com.firstproject.smartinventory.exception.notFound.SaleNotFoundException;
import com.firstproject.smartinventory.mapper.SaleMapper;
import com.firstproject.smartinventory.repository.ProductRepository;
import com.firstproject.smartinventory.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class

SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StoreContextService storeContextService;

    @Autowired
    private StoreAuthorizationService storeAuthorizationService;

    @Override
    public SaleResponseDTO createSale(SaleRequestDTO saleRequestDTO) {
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Store Can't be null");
        storeAuthorizationService.verifyUserAccess(store);
        Sale sale = new Sale();                                 // creating a new sale
        sale.setDate(LocalDateTime.now());                       // Assigning Sale date
        sale.setCustomerName(saleRequestDTO.getCustomerName());  // assign customer name

        List<SaleItems> saleItems = new ArrayList<>();          // Creating a list to Store the  SaleItems
        Double totalAmount = 0.0;                                // Local variable to keep track of total cost of saleItems
        for (SaleItemsRequestDTO itemsDTO : saleRequestDTO.getItems()) {

            Product product = productRepository.findByIdAndStore_StoreId(itemsDTO.getProductId(), store.getStoreId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found with ID " + itemsDTO.getProductId()));

            if (product.getQuantity() < itemsDTO.getQuantity()) {
                throw new InsufficientStockException("Not Enough stock for product " + product.getName());
            }

            product.setQuantity(product.getQuantity() - itemsDTO.getQuantity());
            sale.setStore(store);
            productRepository.save(product);

            SaleItems saleItem = SaleMapper.toEntity(itemsDTO, product);
            saleItem.setSale(sale);
            saleItem.setStore(store);
            saleItems.add(saleItem);

            totalAmount += saleItem.getSubTotal();
        }
        sale.setSaleItems(saleItems);
        sale.setTotalAmount(totalAmount);

        Sale saleSaved = saleRepository.save(sale);

        return SaleMapper.toSaleresponseDTO(saleSaved);
    }

    @Override
    public List<SaleResponseDTO> getAllSales() {
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Store Can't be null");
        storeAuthorizationService.verifyUserAccess(store);
        return saleRepository.findAllSalesByStore(store)
                .stream()
                .map(SaleMapper::toSaleresponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SaleResponseDTO getSaleById(String saleId) {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        if (saleId == null)
            throw new InvalidInputException("Entered SaleId is not Valid");

        Sale sale = saleRepository.getSalesBySaleIdAndStore(saleId, store);

        if (sale == null || sale.getSaleId() == null)
            throw new SaleNotFoundException("Sale not found with ID " + saleId);
        return SaleMapper.toSaleresponseDTO(sale);
    }

    @Override
    public List<SaleResponseDTO> getSalesByDateRange(String startDateStr, String EndDateStr) {
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Store Can't be null");
        storeAuthorizationService.verifyUserAccess(store);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(EndDateStr, formatter);

        return saleRepository.getSalesByDateRangeAndStore(startDate, endDate, store).stream().map(SaleMapper::toSaleresponseDTO).collect(Collectors.toList());

    }
}
