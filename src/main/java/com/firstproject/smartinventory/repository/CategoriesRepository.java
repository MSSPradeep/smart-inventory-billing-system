package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.entity.Categories;
import com.firstproject.smartinventory.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories,String> {

    List<Categories> findByStore(Store store);

    Optional<Categories> findByIdAndStore(String id, Store store);

    boolean findByNameAndStore(String name, Store store);
}
