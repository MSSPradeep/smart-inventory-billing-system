package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.AuthResponseDTO;
import com.firstproject.smartinventory.dto.LoginRequestDTO;
import com.firstproject.smartinventory.dto.RegisterRequestDTO;
import com.firstproject.smartinventory.entity.Role;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.repository.UserRepository;
import com.firstproject.smartinventory.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.IdGenerator;

import java.util.Optional;

import static com.firstproject.smartinventory.others.IDGenerator.idGenerator;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class AuthServicesImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AppUserDetailsService appUserDetailsService;

    @Mock
    private IdGenerator idGenerator;

    @InjectMocks
    private  AuthServiceImpl authServiceImpl;

    private LoginRequestDTO loginRequestDTO;
    private RegisterRequestDTO registerRequestDTO;
    private User user;
    @BeforeEach
    void setUp(){
        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail("testemail1@gmail.com");
        loginRequestDTO.setPassword("testpass");

        registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setEmail("testemail1@gmail.com");
        registerRequestDTO.setPassword("testpass");
        registerRequestDTO.setUsername("testUser");

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
        verify(passwordEncoder).matches(loginRequestDTO.getPassword(), user.getPassword());
    }
    @Test
    void login_failed_wrongCredentials(){
        when(userRepository.findByEmail(loginRequestDTO.getEmail())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authServiceImpl.login(loginRequestDTO));

        assertEquals("No account found on this Email.", exception.getMessage());

    }

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void register_success() {
        when(userRepository.existsByUserNameIgnoreCase(registerRequestDTO.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerRequestDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequestDTO.getPassword())).thenReturn("EncodedPassword");
        when(jwtUtil.generateToken(appUserDetailsService.loadUserByUsername(registerRequestDTO.getUsername())))
                .thenReturn("Generated New JWT Token");

        AuthResponseDTO token = authServiceImpl.register(registerRequestDTO);

        assertNotNull(token);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertEquals(registerRequestDTO.getUsername(), savedUser.getUserName());
        assertEquals(registerRequestDTO.getEmail(), savedUser.getEmail());
        assertEquals("EncodedPassword", savedUser.getPassword());
        assertEquals(Role.OWNER, savedUser.getRole());
        assertNotNull(savedUser.getId());
    }

    @Test
    void register_throwException_mailIdAlreadyExists(){
        when(userRepository.existsByEmail(registerRequestDTO.getEmail())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authServiceImpl.register(registerRequestDTO));

        assertEquals(registerRequestDTO.getEmail()+" is already exist, Please signIn.",exception.getMessage());
    }

    @Test
    void register_throwsException_userNameIsAlreadyExist(){
        when(userRepository.existsByUserNameIgnoreCase(registerRequestDTO.getUsername())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> authServiceImpl.register(registerRequestDTO));

        assertEquals( registerRequestDTO.getUsername()+" is already exist in database.", exception.getMessage());
    }

}
