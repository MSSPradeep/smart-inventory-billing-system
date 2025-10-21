package com.firstproject.smartinventory.dto;

import com.firstproject.smartinventory.entity.SaleItems;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaleRequestDTO {

    @NotBlank(message = "name is required")
    @Size(min = 3, message = "user name contains al least 3 characters.")
    private String customerName;

    private List<SaleItemsRequestDTO> items;

}
