package com.firstproject.smartinventory.dto;

import lombok.Data;

@Data
public class RegisterRequestDTO {

    private String username;
    private String email;
    private String password;
}
