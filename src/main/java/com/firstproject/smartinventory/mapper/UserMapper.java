package com.firstproject.smartinventory.mapper;

import com.firstproject.smartinventory.dto.UserRequestDTO;
import com.firstproject.smartinventory.dto.UserResponseDTO;
import com.firstproject.smartinventory.entity.User;

public class UserMapper {

//this method will convert the dto -> entity
    public static User toEntity(UserRequestDTO userRequestDTO){
        User user = new User();
        user.setRole(userRequestDTO.getRole());
        user.setUserName(userRequestDTO.getUserName());
        user.setPassword(userRequestDTO.getPassword());
        return user;
    }

//this method will convert the dto -> entity
    public static UserResponseDTO toDTO(User user){
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUserName(user.getUserName());
        userResponseDTO.setRole(user.getRole());
        userResponseDTO.setId(user.getId());
        return userResponseDTO;
    }

}
