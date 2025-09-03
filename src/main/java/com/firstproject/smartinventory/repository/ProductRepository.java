package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
