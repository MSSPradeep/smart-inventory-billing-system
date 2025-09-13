package com.firstproject.smartinventory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequestDTO {

    @NotBlank(message = "Store name is required")
    private String storeName;

    @NotBlank(message = "Store address is required")
    private String storeAddress;

}
