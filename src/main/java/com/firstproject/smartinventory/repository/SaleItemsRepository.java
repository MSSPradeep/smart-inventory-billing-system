package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.entity.SaleItems;
import com.firstproject.smartinventory.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemsRepository extends JpaRepository<SaleItems, String>{


    List<SaleItems> findBySale_SaleIdAndStore(String id, Store store);

    List<SaleItems> findByProduct_IdAndStore(String productId, Store store);

    @Query("SELECT SUM(s.quantity) FROM SaleItems s WHERE s.product.id = :productId AND s.store=:store")
    Integer getTotalQuantitySoldByProductAndStore(String productId,Store store);

    SaleItems findByIdAndStore(String id, Store store);
}
