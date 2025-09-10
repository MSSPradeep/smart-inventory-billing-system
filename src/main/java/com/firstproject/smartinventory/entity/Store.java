package com.firstproject.smartinventory.entity;

import com.firstproject.smartinventory.others.IDGenerator;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Store {

    @Id
    private String storeId;

    private String storeName;

    private String storeAddress;

    @ManyToOne
    private User owner;

    @PrePersist
    public void prePesrsist(){
        if(storeId == null)
            this.storeId = IDGenerator.idGenerator("STORE");
    }
}
