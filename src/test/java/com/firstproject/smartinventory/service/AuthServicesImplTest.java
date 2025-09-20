package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.AuthResponseDTO;
import com.firstproject.smartinventory.dto.LoginRequestDTO;
import com.firstproject.smartinventory.entity.Role;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.repository.UserRepository;
import com.firstproject.smartinventory.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServicesImplTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AppUserDetailsService appUserDetailsService;

    @Autowired
    private StoreContextService storeContextService;

    @Autowired
    private StoreAuthorizationService storeAuthorizationService;

    @InjectMocks
    private  AuthServiceImpl authServiceImpl;

    private LoginRequestDTO loginRequestDTO;
    private User user;
    @BeforeEach
    void setUp(){
        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("testemail1@gmail.com");
        loginRequestDTO.setPassword("testpass");

        user = new User();
        user.setUserName("testName");
        user.setPassword("testPassword");
        user.setId("USER20250909");
        user.setRole(Role.ADMIN);
        user.setEmail("testemail1@gmail.com");
    }

    @Test
    void login_success(){
        when(userRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(appUserDetailsService.loadUserByUsername(user.getUserName()))).thenReturn("mocked-JWT-token");

        AuthResponseDTO responseDTO = authServiceImpl.login(loginRequestDTO);

        assertEquals("mocked-JWT-token",responseDTO.getToken());
    }
}
