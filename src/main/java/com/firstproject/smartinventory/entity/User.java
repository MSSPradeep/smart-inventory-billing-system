package com.firstproject.smartinventory.entity;

import com.firstproject.smartinventory.others.IDGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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

    @PrePersist
    public void prePersist(){
        if(id == null)
            this.id = IDGenerator.idGenerator("USER");
    }
}
