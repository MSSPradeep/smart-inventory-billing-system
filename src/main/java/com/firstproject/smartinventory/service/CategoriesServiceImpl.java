package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.exception.badRequest.InvalidInputException;
import com.firstproject.smartinventory.exception.notFound.CategoriesNotFoundException;
import com.firstproject.smartinventory.mapper.CategoriesMapper;
import com.firstproject.smartinventory.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CategoriesServiceImpl implements CategoriesService {

    @Autowired
    private StoreContextService storeContextService;

    @Autowired
    private StoreAuthorizationService storeAuthorizationService;

    @Autowired
    private  CategoriesRepository categoriesRepository;

    public List<CategoriesDTO> getAllCategories() {
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Store Can't be null");
        storeAuthorizationService.verifyUserAccess(store);
        return categoriesRepository.findByStore_StoreId(store.getStoreId()).stream().map(CategoriesMapper::toDTO).toList();
    }

    public CategoriesDTO saveCategories(CategoriesDTO categoriesDTO) {
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Store Can't be null");
        storeAuthorizationService.verifyUserAccess(store);
        Categories categories = CategoriesMapper.toEntity(categoriesDTO);
        categories.setStore(store);
        Categories saved = categoriesRepository.save(categories);
        return CategoriesMapper.toDTO(saved);
    }

    @Override
    public CategoriesDTO updateCategory(String id, CategoriesDTO categoriesDTO) {
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Store Can't be null");
        storeAuthorizationService.verifyUserAccess(store);
        Categories category = categoriesRepository.findByIdAndStore_StoreId(id, store.getStoreId())
                .orElseThrow(() -> new CategoriesNotFoundException("Category is not available with id " + id));

        if (categoriesDTO.getName() != null)
            category.setName(categoriesDTO.getName());
        Categories saved = categoriesRepository.save(category);
        return CategoriesMapper.toDTO(saved);

    }

    @Override
    public void deleteCategory(String id) {
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Store Can't be null");
        storeAuthorizationService.verifyUserAccess(store);
        Categories category = categoriesRepository.findByIdAndStore_StoreId(id, store.getStoreId())
                .orElseThrow(() -> new CategoriesNotFoundException("No category is available with this id " + id));

        categoriesRepository.delete(category);
    }


}
