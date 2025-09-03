package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.entity.SaleItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemsRepository extends JpaRepository<SaleItems, String>{


    List<SaleItems> findBySale_SaleId(String id);

    List<SaleItems> findByProduct_Id(String productId);
    @Query("SELECT SUM(s.subTotal) FROM SaleItems s WHERE s.product.id = :productId")
    Integer getTotalQuantitySoldByProduct(String productId);
}
