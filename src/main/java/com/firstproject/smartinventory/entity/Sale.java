package com.firstproject.smartinventory.entity;

import com.firstproject.smartinventory.others.IDGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sale")
public class Sale {
    @Id
    private String saleId;

    private LocalDateTime date;

    private Double totalAmount;

    private String customerName;

    @OneToMany(mappedBy = "sale",cascade = CascadeType.ALL)
    private List<SaleItems> saleItems = new ArrayList<>();

    @PrePersist
    public void prePersist(){
        if(saleId == null)
            this.saleId = IDGenerator.idGenerator("SAL");
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
