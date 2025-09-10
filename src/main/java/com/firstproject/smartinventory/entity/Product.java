package com.firstproject.smartinventory.entity;


import com.firstproject.smartinventory.others.IDGenerator;
import jakarta.persistence.*;
import lombok.*;
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    private String id;

    @Column(nullable = false )
    private String name;

    private Double price;

    @Column(name = "stock_qty")
    private Integer quantity;

    private String brand;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Categories categories;

    @PrePersist
    public void prePersist(){
        if(id == null)
            this.id = IDGenerator.idGenerator("PRO");
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
