package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.exception.badRequest.DuplicateEntryException;
import com.firstproject.smartinventory.exception.badRequest.InvalidInputException;
import com.firstproject.smartinventory.exception.notFound.CategoriesNotFoundException;
import com.firstproject.smartinventory.exception.notFound.ProductNotFoundException;
import com.firstproject.smartinventory.mapper.ProductMapper;
import com.firstproject.smartinventory.repository.CategoriesRepository;
import com.firstproject.smartinventory.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoriesRepository categoriesRepository;

    @Autowired
    private StoreContextService storeContextService;

    @Autowired
    private StoreAuthorizationService storeAuthorizationService;

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        if(store == null)
            throw new InvalidInputException("Invalid Store Details");

        if(productDTO.getName() == null) {
            throw new InvalidInputException("Product name cannot be null");
        }
        if(productRepository.existsByNameAndBrandAndStore_StoreId(productDTO.getName(),productDTO.getBrand(), store.getStoreId()))
            throw new DuplicateEntryException("Product is already exists in database.");
        Product product = ProductMapper.toEntity(productDTO);
        product.setStore(store);
        if(productDTO.getCategoryId() != null) {
            Categories category = categoriesRepository.findByIdAndStore_StoreId(productDTO.getCategoryId(),store.getStoreId())
                    .orElseThrow(() -> new CategoriesNotFoundException("CategoriesId not found " + productDTO.getCategoryId()));
            product.setCategories(category);
        }
        Product saved = productRepository.save(product);

        return ProductMapper.toDTO(saved);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Invalid Store Details");
        storeAuthorizationService.verifyUserAccess(store);
        return productRepository.findByStore_StoreId(store.getStoreId()).stream().map(ProductMapper::toDTO).toList();
    }

    @Override
    public ProductDTO getProductById(String id) {
        if(id == null)
            throw new InvalidInputException("ID can't be null");

        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new InvalidInputException("Invalid Store Details");

        storeAuthorizationService.verifyUserAccess(store);

        Optional<Product> optional = productRepository.findByIdAndStore_StoreId(id,store.getStoreId());
        if(optional.isEmpty())
            throw new ProductNotFoundException("Product is not available with id "+id);
        else
            return ProductMapper.toDTO(optional.get());
    }

    @Override
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        if(store == null)
            throw new InvalidInputException("Store can't be null");
        Product product = productRepository.findByIdAndStore_StoreId(id, store.getStoreId())
                .orElseThrow(() -> new ProductNotFoundException("Product is not Available "+id));

        if(productDTO.getName() != null)
            product.setName(productDTO.getName());
        if(productDTO.getQuantity() >= 1)
            product.setQuantity(productDTO.getQuantity());
        if(productDTO.getBrand() != null)
            product.setBrand(productDTO.getBrand());
        if(productDTO.getPrice() >= 1)
            product.setPrice(productDTO.getPrice());
        if(productDTO.getCategoryId() != null) {
            Categories category = categoriesRepository.findByIdAndStore_StoreId(productDTO.getCategoryId(),store.getStoreId())
                    .orElseThrow(() -> new CategoriesNotFoundException("CategoriesId not found " + productDTO.getCategoryId()));
            product.setCategories(category);
        }
        productRepository.save(product);
        return ProductMapper.toDTO(product);
    }

    @Override
    public void deleteProduct(String id) {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        if(store == null)
            throw new InvalidInputException("Store can't be  null");
        Optional<Product> optionalProduct = productRepository.findByIdAndStore_StoreId(id, store.getStoreId());
        if(optionalProduct.isEmpty())
            throw new ProductNotFoundException("Product is not found with id "+id);

        productRepository.delete(optionalProduct.get());
    }
}
