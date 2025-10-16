package com.firstproject.smartinventory.entity;

import com.firstproject.smartinventory.others.IDGenerator;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
    @ToString.Exclude                   //
    @EqualsAndHashCode.Exclude          // these lines are used to avoid recursion and prevent stack overflow
    private User owner;

    @PrePersist
    public void prePesrsist(){
        if(storeId == null)
            this.storeId = IDGenerator.idGenerator("STORE");
    }

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude                   //
    @EqualsAndHashCode.Exclude          // these lines are used to avoid recursion and prevent stack overflow
    private Set<User> employee = new HashSet<>();
}
