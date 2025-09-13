package com.firstproject.smartinventory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreResponseDTO {


    private String storeId;
    private String storeName;
    private String storeAddress;
    private String ownerId;
    private String ownerName;
}
