package com.firstproject.smartinventory.mapper;

import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.others.IDGenerator;
import org.springframework.util.IdGenerator;

public class CategoriesMapper {

//    This method is to convert DTO -> Entity
    public static Categories toEntity(CategoriesDTO categoriesDTO){
        Categories categories = new Categories();
        categories.setName(categoriesDTO.getName());
        categories.setId(IDGenerator.idGenerator("CAT"));
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
