package com.firstproject.smartinventory.mapper;

import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.entity.Categories;

public class CategoriesMapper {

//    This method is to convert DTO -> Entity
    public static Categories toEntity(CategoriesDTO categoriesDTO){
        Categories categories = new Categories();
        categories.setName(categoriesDTO.getName());
        return categories;
    }

//    This method is to convert Entity -> DTO
    public static CategoriesDTO toDTO(Categories categories){
        CategoriesDTO categoriesDTO = new CategoriesDTO();
        categoriesDTO.setName(categories.getName());
        categoriesDTO.setId(categories.getId());
        return  categoriesDTO;
    }
}
