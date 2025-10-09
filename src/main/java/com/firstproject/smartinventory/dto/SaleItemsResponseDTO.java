package com.firstproject.smartinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemsResponseDTO {

    private String id;
    private String productId;
    private String productName;
    private int quantity;
    private double price;
    private double totalAmount;
}
