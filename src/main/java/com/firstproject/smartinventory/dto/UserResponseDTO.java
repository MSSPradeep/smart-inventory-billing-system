package com.firstproject.smartinventory.dto;


import com.firstproject.smartinventory.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String id;
    private String userName;
    private Role role;
    private String email;

}
