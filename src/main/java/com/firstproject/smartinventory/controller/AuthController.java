package com.firstproject.smartinventory.controller;

import com.firstproject.smartinventory.dto.AuthResponseDTO;
import com.firstproject.smartinventory.dto.LoginRequestDTO;
import com.firstproject.smartinventory.dto.RegisterRequestDTO;
import com.firstproject.smartinventory.repository.UserRepository;
import com.firstproject.smartinventory.security.JwtUtil;
import com.firstproject.smartinventory.service.AppUserDetailsService;
import com.firstproject.smartinventory.service.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private AuthServiceImpl authServiceImpl;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(authServiceImpl.login(loginRequestDTO));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO){
        return ResponseEntity.ok(authServiceImpl.register(registerRequestDTO));
    }

}
