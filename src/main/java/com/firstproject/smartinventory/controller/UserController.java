package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.SaleItemsRequestDTO;
import com.firstproject.smartinventory.dto.UserRequestDTO;
import com.firstproject.smartinventory.dto.UserResponseDTO;
import com.firstproject.smartinventory.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;


    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO){
        return ResponseEntity.ok(userServiceImpl.createUser(userRequestDTO));
    }

    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public List<UserResponseDTO> getAllUsers(){
        return userServiceImpl.getAllUsers();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id){
        return ResponseEntity.ok(userServiceImpl.getUserById(id));
    }

    @GetMapping("/{userName}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<UserResponseDTO> getUserByName(@PathVariable String userName){
        return ResponseEntity.ok(userServiceImpl.getUserByUserName(userName));
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable String id, @RequestBody UserRequestDTO userRequestDTO){
        return ResponseEntity.ok(userServiceImpl.updateUser(id,userRequestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public void deleteUser(@PathVariable String id){
        userServiceImpl.deleteUser(id);
    }


}
