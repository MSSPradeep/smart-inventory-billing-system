package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.UserRequestDTO;
import com.firstproject.smartinventory.dto.UserResponseDTO;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.mapper.UserMapper;
import com.firstproject.smartinventory.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO createUser( UserRequestDTO userRequestDTO) {
        if(userRepository.existsByUserNameIgnoreCase(userRequestDTO.getUserName())){
            throw new RuntimeException("User name is already exist");
        }

        User user = UserMapper.toEntity(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        return UserMapper.toDTO(userRepository.save(user));

    }

    @Override
    public UserResponseDTO getUserById(String id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElseThrow(() ->
                        new EntityNotFoundException("User not found"+ id));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User is Not found "+id ));

        if(userRequestDTO.getId() != null)
            user.setId(userRequestDTO.getId());
        if(userRequestDTO.getUserName() != null)
            user.setUserName(userRequestDTO.getUserName());
        if(userRequestDTO.getRole() != null)
            user.setRole(userRequestDTO.getRole());
        if(userRequestDTO.getPassword() != null)
            user.setPassword(userRequestDTO.getPassword());

        User updatedUser = userRepository.save(user);
        return UserMapper.toDTO(updatedUser);
    }

    @Override
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User is Not found "+id ));
        userRepository.delete(user);
    }

    @Override
    public UserResponseDTO getUserByUserName(String name) {
        return userRepository.findAll().stream()
                .filter(user -> user.getUserName().equalsIgnoreCase(name))
                .findFirst()
                .map(user -> new UserResponseDTO(user.getId(),user.getUserName(),user.getRole()))
                .orElseThrow(()-> new UsernameNotFoundException("No user is found with "+name));

    }
}
