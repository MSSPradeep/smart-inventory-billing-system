package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.UserRequestDTO;
import com.firstproject.smartinventory.dto.UserResponseDTO;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.mapper.UserMapper;
import com.firstproject.smartinventory.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.firstproject.smartinventory.entity.Role.ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    User user ;
    @BeforeEach
    void setUp(){
        user = new User();
        user.setUserName("Unit_Test");
        user.setPassword("Unit_test_123");
        user.setId("USER20250909");
        user.setRole(ADMIN);
    }



}
