package com.firstproject.smartinventory.dto;

import com.firstproject.smartinventory.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "userName is required")
    @Size(min = 3, message = "user name should contains at least 3 characters.")
    private String userName;

    @NotNull(message = "role is required")
    private Role role;

    @NotBlank(message = "password is required")
    @Size(min = 6, message = "user name should contains at least 6 characters.")
    private String password;

    @NotBlank(message = "email is required")
    private String email;

    private String id;


}
