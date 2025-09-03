package com.firstproject.smartinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleResponseDTO {

    private String saleId;
    private LocalDateTime date;
    private Double totalAmount;
    private String customerName;
    private List<SaleItemsResponseDTO> items;
}
