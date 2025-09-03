package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.*;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.Sale;
import com.firstproject.smartinventory.entity.SaleItems;
import com.firstproject.smartinventory.mapper.SaleMapper;
import com.firstproject.smartinventory.repository.ProductRepository;
import com.firstproject.smartinventory.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SaleServiceImpl implements SaleService{

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public SaleResponseDTO createSale(SaleRequestDTO saleRequestDTO) {
        // creating a new sale
       Sale sale =  new Sale();
       sale.setDate(LocalDateTime.now());                       // Assigning Sale date
       sale.setCustomerName(saleRequestDTO.getCustomerName());  // assign customer name

        List<SaleItems> saleItems = new ArrayList<>();          // Creating a list to Store the  SaleItems
       Double totalAmount = 0.0;                                // Local variable to keep track of total cost of saleItems

       for(SaleItemsRequestDTO itemsDTO: saleRequestDTO.getItems()){

           Product product = productRepository.findById(itemsDTO.getProductId())
                   .orElseThrow(()-> new RuntimeException("Product not found with ID "+itemsDTO.getProductId()));

           if(product.getQuantity() < itemsDTO.getQuantity()){
               throw new RuntimeException("Not Enough stock for product "+product.getName());
           }

           product.setQuantity(product.getQuantity() - itemsDTO.getQuantity());
           productRepository.save(product);

           SaleItems saleItem = SaleMapper.toEntity(itemsDTO,product);
           saleItem.setSale(sale);

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
        return saleRepository.findAll()
                .stream()
                .map(SaleMapper::toSaleresponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SaleResponseDTO getSaleById(String saleId) {
        return saleRepository.getSalesBySaleId(saleId);
    }

    @Override
    public List<SaleResponseDTO> getSalesByDateRange(String startDateStr, String EndDateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.parse(startDateStr,formatter);
        LocalDateTime endDate = LocalDateTime.parse(EndDateStr,formatter);

        return saleRepository.getSalesByDateRange(startDate,endDate).stream().map(SaleMapper::toSaleresponseDTO).collect(Collectors.toList());

    }
}
