package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.AuthResponseDTO;
import com.firstproject.smartinventory.dto.LoginRequestDTO;
import com.firstproject.smartinventory.dto.RegisterRequestDTO;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.others.IDGenerator;
import com.firstproject.smartinventory.repository.UserRepository;
import com.firstproject.smartinventory.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.firstproject.smartinventory.entity.Role.OWNER;

@Service
public class AuthServiceImpl implements AuthService{

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

    @Override
    public AuthResponseDTO login(LoginRequestDTO dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new RuntimeException("No Account found on this Email."));

        String token =  jwtUtil.generateToken(appUserDetailsService.loadUserByUsername(user.getUserName()));
        return new AuthResponseDTO(token);
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByUserNameIgnoreCase(dto.getUsername())){
            throw new RuntimeException("username already exist");
        }
        if( userRepository.existsByEmail(dto.getEmail())){
            throw new RuntimeException("Email is already exists");
        }

        User user = new User();
        user.setId(IDGenerator.idGenerator("USER"));
        user.setUserName(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(OWNER);

        userRepository.save(user);

        String token = jwtUtil.generateToken(appUserDetailsService.loadUserByUsername(user.getUserName()));
        return new AuthResponseDTO(token);
    }
}
