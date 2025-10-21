package com.firstproject.smartinventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesDTO{


    private String id;
    @NotBlank(message = "name is Required" )
    @Size(min = 3,message = "Categories name must be at least 3 characters")
    private String name;

}
