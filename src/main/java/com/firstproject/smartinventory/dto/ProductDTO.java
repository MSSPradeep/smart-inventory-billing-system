package com.firstproject.smartinventory.dto;



import com.firstproject.smartinventory.entity.Categories;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String id;

    @NotBlank(message = " name is required")
    @Size(min = 3, message = "name must contains at least 3 characters.")
    private String name;

    @NotBlank(message = "brand is required")
    @Size(min = 3, message = "brand must contains at least 3 characters.")
    private String brand;

    @DecimalMin(value = "0.1", message = "price is greater than 0 ")
    private double price;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;


//    private CategoriesDTO categories;

    private String categoryId;
}
