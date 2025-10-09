package com.firstproject.smartinventory.entity;

import com.firstproject.smartinventory.others.IDGenerator;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private String id;

    @Column(name = "user_name",nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, unique = true)
    private String email;

    @PrePersist
    public void prePersist(){
        if(id == null)
            this.id = IDGenerator.idGenerator("USER");
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = true)
    private Store store;
}
