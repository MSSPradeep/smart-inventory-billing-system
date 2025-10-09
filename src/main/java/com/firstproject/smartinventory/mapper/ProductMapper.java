package com.firstproject.smartinventory.mapper;

import com.firstproject.smartinventory.dto.CategoriesDTO;
import com.firstproject.smartinventory.dto.ProductDTO;
import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.others.IDGenerator;
import com.firstproject.smartinventory.repository.CategoriesRepository;
import com.firstproject.smartinventory.service.CategoriesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductMapper {

    @Autowired
    private CategoriesServiceImpl categoriesService;

    // This method is to convert th DTO -> Entity.
    public static Product toEntity(ProductDTO dto){
        Product product = new Product();
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setQuantity(dto.getQuantity());
        product.setPrice(dto.getPrice());
        product.setId(IDGenerator.idGenerator("PROD"));
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

        if(entity.getCategories()!= null){
            productDTO.setCategoryId(entity.getCategories().getId());
        }

        return productDTO;
    }
}
