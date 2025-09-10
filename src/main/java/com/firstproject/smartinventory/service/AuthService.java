package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.AuthResponseDTO;
import com.firstproject.smartinventory.dto.LoginRequestDTO;
import com.firstproject.smartinventory.dto.RegisterRequestDTO;

public interface AuthService {

    AuthResponseDTO login(LoginRequestDTO dto);

    AuthResponseDTO register(RegisterRequestDTO dto);
}
