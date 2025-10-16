package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories,String> {

    List<Categories> findByStore_StoreId(String storeId);

    Optional<Categories> findByIdAndStore_StoreId(String id, String storeId);

    boolean existsByNameAndStore_StoreId(String name, String storeId);
}
