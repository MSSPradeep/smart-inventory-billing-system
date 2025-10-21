package com.firstproject.smartinventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreRequestDTO {

    @NotBlank(message = "Store name is required")
    @Size(min = 3, message = "store name must be contains at least 3 letters.")
    private String storeName;

    @NotBlank(message = "Store address is required")
    @Size(min = 3, message = "store address must be contains at least 3 letters.")
    private String storeAddress;

}
