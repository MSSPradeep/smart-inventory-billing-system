package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.entity.Product;
import com.firstproject.smartinventory.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByStore(Store store);

    Optional<Product> findByIdAndStore(String id, Store store);

    boolean existsByNameAndStore(String name, Store store);
}
