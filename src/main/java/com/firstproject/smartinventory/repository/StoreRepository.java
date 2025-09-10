package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store,String>{

    List<Store> findByOwner(User owner);
}
