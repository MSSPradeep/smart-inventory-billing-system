package com.firstproject.smartinventory.entity;

import com.firstproject.smartinventory.others.IDGenerator;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Categories {
    @Id

    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    @PrePersist
    public void prePersist(){
        if(id == null)
            this.id = IDGenerator.idGenerator("CAT");
    }
}
