package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories,String> {



}
