package com.firstproject.smartinventory.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesDTO{

    private String id;
    @NotBlank(message = "name is Required")
    private String name;

}
