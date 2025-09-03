package com.firstproject.smartinventory.mapper;

import com.firstproject.smartinventory.dto.CategoryDTO;
import com.firstproject.smartinventory.entity.Categories;

public class CategoryMapper {
    public static CategoryDTO toDTO(Categories categories){
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(categories.getId());
        categoryDTO.setName(categories.getName());
        return categoryDTO;
    }

    public static Categories toEntity(CategoryDTO dto){
        Categories categories = new Categories();
        categories.setName(dto.getName());
        categories.setId(dto.getId());
        return categories;
    }
}
