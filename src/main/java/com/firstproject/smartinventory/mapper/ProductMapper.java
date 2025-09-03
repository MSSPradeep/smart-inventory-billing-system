package com.firstproject.smartinventory.mapper;

import com.firstproject.smartinventory.dto.CategoryDTO;
import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.entity.Product;

public class ProductMapper {

    // This method is to convert th DTO -> Entity.
    public static Product toEntity(ProductDTO dto){
        Product product = new Product();
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());

        return product;
    }
    // This method is to convert Entity -> DTO
    public static ProductDTO toDTO(Product entity){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(entity.getName());
        productDTO.setBrand(entity.getBrand());
        productDTO.setPrice(entity.getPrice());
        productDTO.setQuantity(entity.getQuantity());
        productDTO.setId(entity.getId());

        if(entity.getCategories() != null){
            // Map entity category to DTO
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(entity.getCategories().getId());
            categoryDTO.setName(entity.getCategories().getName());
            productDTO.setCategories(CategoryMapper.toEntity(categoryDTO));

            // Also set categoryId for reference
            productDTO.setCategoryId(entity.getCategories().getId());
        }

        return productDTO;
    }
}
