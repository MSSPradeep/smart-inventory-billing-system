package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.mapper.CategoriesMapper;
import com.firstproject.smartinventory.repository.CategoriesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceImpl extends Categories {

    private final CategoriesRepository categoriesRepository;

    public CategoriesServiceImpl(CategoriesRepository categoriesRepository){
        this.categoriesRepository = categoriesRepository;
    }


    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }
    public CategoriesDTO saveCategories(CategoriesDTO categoriesDTO) {
         Categories categories = CategoriesMapper.toEntity(categoriesDTO);
         Categories saved = categoriesRepository.save(categories);
         return CategoriesMapper.toDTO(saved);
    }


}
