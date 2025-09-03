package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.SaleItemsRequestDTO;
import com.firstproject.smartinventory.dto.UserRequestDTO;
import com.firstproject.smartinventory.dto.UserResponseDTO;
import com.firstproject.smartinventory.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;


    @PostMapping
    public UserResponseDTO createUser(@RequestBody UserRequestDTO userRequestDTO){
        return userServiceImpl.createUser(userRequestDTO);
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers(){
        return userServiceImpl.getAllUsers();
    }

    @GetMapping("/id/{id}")
    public UserResponseDTO getUserById(@PathVariable String id){
        return userServiceImpl.getUserById(id);
    }

    @GetMapping("/{userName}")
    public UserResponseDTO getUserByName(@PathVariable String userName){
        return userServiceImpl.getUserByUserName(userName);
    }
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(@PathVariable String id, @RequestBody UserRequestDTO userRequestDTO){
        return userServiceImpl.updateUser(id,userRequestDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        userServiceImpl.deleteUser(id);
    }


}
