package com.firstproject.smartinventory.repository;

import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByUserNameIgnoreCase(String userName);

    boolean existsByUserNameIgnoreCaseAndStore(String userName, Store store);

    boolean existsByUserNameIgnoreCase(String userName);

    boolean existsByEmail(String email);

    boolean existsByEmailAndStore(String email,Store store);

    Optional<User> findByIdAndStore(String id, Store store);

    Optional<User> findByEmail(String email);

    List<User> findAllUsersByStore(Store store);

}
