package com.firstproject.smartinventory.mapper;

import com.firstproject.smartinventory.dto.SaleItemsRequestDTO;
import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.dto.SaleResponseDTO;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.Sale;
import com.firstproject.smartinventory.entity.SaleItems;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;


public class SaleMapper {

    // this method is to convert the DTO -> Entity
    public static SaleItems toEntity(SaleItemsRequestDTO dto, Product product){
        SaleItems saleItems = new SaleItems();

        saleItems.setProduct(product);
        saleItems.setQuantity(dto.getQuantity());
        saleItems.setPrice(product.getPrice());
        saleItems.setSubTotal(product.getPrice() * saleItems.getQuantity());
        return saleItems;
    }

    // This method is to convert the Entity -> saleResponseDTO
    public static SaleResponseDTO toSaleresponseDTO(Sale sale){
        SaleResponseDTO dto = new SaleResponseDTO();
        dto.setSaleId(sale.getSaleId());
        dto.setDate(sale.getDate());
        dto.setTotalAmount(sale.getTotalAmount());
        dto.setCustomerName(sale.getCustomerName());
        List<SaleItemsResponseDTO> itemDTO = sale.getSaleItems()
                .stream()
                .map(SaleMapper::toSaleItemsResponseDTO)
                .toList();
        dto.setItems(itemDTO);
        return dto;
    }
    //This method is to convert the Entity -> SaleItemsResponseDTO
    private static SaleItemsResponseDTO toSaleItemsResponseDTO(SaleItems saleItems) {

        SaleItemsResponseDTO dto = new SaleItemsResponseDTO();
        dto.setProductId(saleItems.getProduct().getId());
        dto.setId(saleItems.getId());
        dto.setProductName(saleItems.getProduct().getName());
        dto.setQuantity(saleItems.getQuantity());
        dto.setPrice(saleItems.getPrice());
        dto.setTotalAmount(saleItems.getSubTotal());

        return dto;
    }

}
