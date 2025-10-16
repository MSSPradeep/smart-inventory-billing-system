package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByStore_StoreId(String storeId);

    Optional<Product> findByIdAndStore_StoreId(String id, String storeId);

    boolean existsByNameAndStore_StoreId(String name, String storeId);

    boolean existsByNameAndBrandAndStore_StoreId(String name, String brand, String storeId);
}
