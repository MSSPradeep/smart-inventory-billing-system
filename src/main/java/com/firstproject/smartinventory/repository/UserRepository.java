package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByUserNameIgnoreCase(String userName);

    boolean existsByUserNameIgnoreCase(String userName);

    User save(User user);
}
