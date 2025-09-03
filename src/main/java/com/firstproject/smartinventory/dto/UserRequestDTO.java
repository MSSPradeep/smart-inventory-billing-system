package com.firstproject.smartinventory.dto;

import com.firstproject.smartinventory.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "userName is required")
    private String userName;

    @NotBlank(message = "role is required")
    private Role role;

    @NotBlank(message = "password is required")
    private String password;

    private String id;


}
