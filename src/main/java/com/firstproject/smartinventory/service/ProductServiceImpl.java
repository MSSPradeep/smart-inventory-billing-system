package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.mapper.ProductMapper;
import com.firstproject.smartinventory.repository.CategoriesRepository;
import com.firstproject.smartinventory.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

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
        if(productDTO.getName() == null)
            throw new RuntimeException("Product name cannot be null");

        if(store == null)
            throw new IllegalArgumentException("Invalid Store Details");

        Product product = ProductMapper.toEntity(productDTO);
        product.setStore(store);
        if(productDTO.getCategoryId() != null) {
            Categories category = categoriesRepository.findByIdAndStore(productDTO.getCategoryId(),store)
                    .orElseThrow(() -> new RuntimeException("CategoriesId not found " + productDTO.getCategoryId()));
            product.setCategories(category);
        }
        Product saved = productRepository.save(product);

        return ProductMapper.toDTO(saved);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new IllegalArgumentException("Invalid Store Details");
        storeAuthorizationService.verifyUserAccess(store);
        return productRepository.findByStore(store).stream().map(ProductMapper::toDTO).toList();
    }

    @Override
    public ProductDTO getProductById(String id) {
        if(id == null)
            throw new IllegalArgumentException("ID can't be null");

        Store store = storeContextService.getCurrentStore();
        if(store == null)
            throw new IllegalArgumentException("Invalid Store Details");

        storeAuthorizationService.verifyUserAccess(store);

        return productRepository.findByIdAndStore(id,store).map(ProductMapper::toDTO)
                .orElseThrow(()->new IllegalArgumentException("Product is not available with id "+id));
    }

    @Override
    public ProductDTO updateProduct(String id, ProductDTO productDTO) {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        if(store == null)
            throw new IllegalArgumentException("Store can't be null");
        Product product = productRepository.findByIdAndStore(id, store)
                .orElseThrow(() -> new RuntimeException("Product is not Available "+id));

        if(productDTO.getName() != null)
            product.setName(productDTO.getName());
        if(productDTO.getQuantity() >= 1)
            product.setQuantity(productDTO.getQuantity());
        if(productDTO.getBrand() != null)
            product.setBrand(productDTO.getBrand());
        if(productDTO.getPrice() >= 1)
            product.setPrice(productDTO.getPrice());
        if(productDTO.getCategoryId() != null) {
            Categories category = categoriesRepository.findByIdAndStore(productDTO.getCategoryId(),store)
                    .orElseThrow(() -> new RuntimeException("CategoriesId not found " + productDTO.getCategoryId()));
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
            throw new IllegalArgumentException("Store can't be  null");
        Optional<Product> optionalProduct = productRepository.findByIdAndStore(id, store);
        if(optionalProduct.isEmpty())
            throw new RuntimeException("Product is not found with id "+id); 

        productRepository.delete(optionalProduct.get());
    }
}
