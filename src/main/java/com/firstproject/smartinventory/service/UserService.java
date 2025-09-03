package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.UserRequestDTO;
import com.firstproject.smartinventory.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO userRequestDTO);

    UserResponseDTO getUserById(String id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(String id, UserRequestDTO userRequestDTO);

    void deleteUser(String id);

    UserResponseDTO getUserByUserName(String name);
}
