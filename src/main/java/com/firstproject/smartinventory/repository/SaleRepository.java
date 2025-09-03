package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.dto.SaleResponseDTO;
import com.firstproject.smartinventory.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale,String> {

    SaleResponseDTO getSalesBySaleId(String SaleId);

    @Query("SELECT s FROM Sale s WHERE s.date  BETWEEN :startDate AND :endDate")
    List<Sale> getSalesByDateRange(@Param("startDate") LocalDateTime startDate,
                                               @Param("endDate")  LocalDateTime endDate);
}
