package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.dto.SaleResponseDTO;
import com.firstproject.smartinventory.entity.Sale;
import com.firstproject.smartinventory.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale,String> {

    Sale getSalesBySaleIdAndStore(String SaleId, Store store);

    @Query("SELECT s FROM Sale s WHERE s.date  BETWEEN :startDate AND :endDate AND s.store = :store")
    List<Sale> getSalesByDateRangeAndStore(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate")  LocalDateTime endDate,
                                               @Param("store") Store store);


    List<Sale> findAllSalesByStore(Store store);
}
