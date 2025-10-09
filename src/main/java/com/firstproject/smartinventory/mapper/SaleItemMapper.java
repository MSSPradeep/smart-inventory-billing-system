package com.firstproject.smartinventory.mapper;

import com.firstproject.smartinventory.dto.SaleItemsRequestDTO;
import com.firstproject.smartinventory.dto.SaleItemsResponseDTO;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.SaleItems;
import com.firstproject.smartinventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import static com.firstproject.smartinventory.others.IDGenerator.idGenerator;


import java.util.Optional;

public class SaleItemMapper {

    @Autowired
    private static ProductRepository productRepository;


    //This is to convert the DTO to Entity
    public static SaleItemsResponseDTO toDTO(SaleItems saleItems){
            SaleItemsResponseDTO dto = new SaleItemsResponseDTO();
            dto.setId(saleItems.getId());
            dto.setProductId(saleItems.getProduct().getId());
            dto.setProductName(saleItems.getProduct().getName());
            dto.setQuantity(saleItems.getQuantity());
            dto.setPrice(saleItems.getPrice());
            dto.setTotalAmount(saleItems.getSubTotal());
            return dto;
    }

    // This is to convert the Entity into DTO
    public static  SaleItems toEntity(SaleItemsRequestDTO dto){
        SaleItems saleItems = new SaleItems();

        Optional<Product> product = productRepository.findById(dto.getProductId());
        product.ifPresent(saleItems::setProduct);
        saleItems.setPrice(saleItems.getProduct().getPrice());
        saleItems.setSubTotal(saleItems.getPrice() * dto.getQuantity());
        saleItems.setQuantity(dto.getQuantity());
        saleItems.setId(idGenerator("SI"));
        return saleItems;
    }
}
