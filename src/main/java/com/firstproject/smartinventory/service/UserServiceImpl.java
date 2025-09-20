package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.UserRequestDTO;
import com.firstproject.smartinventory.dto.UserResponseDTO;
import com.firstproject.smartinventory.entity.Store;
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

    @Autowired
    private  StoreContextService storeContextService;

    @Autowired
    private StoreAuthorizationService storeAuthorizationService;

    @Override
    public UserResponseDTO createUser( UserRequestDTO userRequestDTO) {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        if(userRepository.existsByUserNameIgnoreCaseAndStore(userRequestDTO.getUserName(),store)){
            throw new RuntimeException("User name is already exist.");
        }
        if(userRepository.existsByEmailAndStore(userRequestDTO.getEmail(), store))
            throw new IllegalArgumentException("Email already exists in database.");

        User user = UserMapper.toEntity(userRequestDTO);
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        userRepository.save(user);
        return UserMapper.toDTO(user);

    }

    @Override
    public UserResponseDTO getUserById(String id) {

        if(id == null)
            throw new IllegalArgumentException("Entered ID is not valid.");
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        return userRepository.findByUserIdAndStore(id,store)
                .map(UserMapper::toDTO)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found with ID "+ id));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        return userRepository.findAllUsersByStore(store)
                .stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO updateUser(String id, UserRequestDTO userRequestDTO) {
        if(id == null)
            throw new IllegalArgumentException("Enter a valid userID");

        Store store =storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);

        User user = userRepository.findByUserIdAndStore(id,store)
                .orElseThrow(()-> new UsernameNotFoundException("User is not found "+id ));

        if(userRequestDTO.getUserName() != null)
            user.setUserName(userRequestDTO.getUserName());
        if(userRequestDTO.getRole() != null)
            user.setRole(userRequestDTO.getRole());
        if(userRequestDTO.getPassword() != null)
            user.setPassword(userRequestDTO.getPassword());
        if(userRequestDTO.getEmail()!=null)
            user.setEmail(userRequestDTO.getEmail());
        if(userRequestDTO.getId()!=null){
            user.setId(userRequestDTO.getId());
        }
        userRepository.save(user);
        return UserMapper.toDTO(user);
    }

    @Override
    public void deleteUser(String id) {
        Store store = storeContextService.getCurrentStore();
        storeAuthorizationService.verifyUserAccess(store);
        User user = userRepository.findByUserIdAndStore(id,store)
                .orElseThrow(()-> new RuntimeException("User is not found "+id ));
        userRepository.delete(user);
    }

    @Override
    public UserResponseDTO getUserByUserName(String name) {
        return userRepository.findAll().stream()
                .filter(user -> user.getUserName().equalsIgnoreCase(name))
                .findFirst()
                .map(user -> new UserResponseDTO(user.getId(),user.getUserName(),user.getRole(), user.getEmail()))
                .orElseThrow(()-> new UsernameNotFoundException("No user is found with "+name));

    }
}
