package com.firstproject.smartinventory.service;

import com.firstproject.smartinventory.dto.UserRequestDTO;
import com.firstproject.smartinventory.dto.UserResponseDTO;
import com.firstproject.smartinventory.entity.Store;
import com.firstproject.smartinventory.entity.User;
import com.firstproject.smartinventory.mapper.UserMapper;
import com.firstproject.smartinventory.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.firstproject.smartinventory.entity.Role.ADMIN;
import static com.firstproject.smartinventory.entity.Role.STAFF;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StoreContextService storeContextService;

    @Mock
    private StoreAuthorizationService storeAuthorizationService;


    @InjectMocks
    private UserServiceImpl userServiceImpl;

    UserRequestDTO userRequestDTO;
    UserResponseDTO userResponseDTO;
    private Store store;
    private User user;

    @BeforeEach
    void setUp(){
        userRequestDTO = new UserRequestDTO();
        userRequestDTO.setUserName("Unit_Test");
        userRequestDTO.setPassword("Unit_test_123");
        userRequestDTO.setId("USER20250909");
        userRequestDTO.setRole(ADMIN);
        userRequestDTO.setEmail("testemail1@gmail.com");

        store = new Store();
        store.setStoreName("test_Store");
        store.setStoreAddress("store_Address");


        user = UserMapper.toEntity(userRequestDTO);
        userResponseDTO = UserMapper.toDTO(user);
    }

    @Test
    void createUser_Success(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(userRepository.existsByUserNameIgnoreCaseAndStore(userRequestDTO.getUserName(),store)).thenReturn(false);
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("Encoded password");
        User newUser = user;
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        UserResponseDTO savedUser = userServiceImpl.createUser(userRequestDTO);

        assertNotNull(savedUser);
        assertEquals(userRequestDTO.getUserName(), savedUser.getUserName());
        assertEquals(userRequestDTO.getRole(), savedUser.getRole());

        verify(storeAuthorizationService, times(1)).verifyUserAccess(store);
        verify(userRepository,times(1)).existsByUserNameIgnoreCaseAndStore(userRequestDTO.getUserName(), store);
        verify(userRepository,times(1)).save(newUser);
    }

    @Test
    void createUserMethodThrowException_userNameIsExistsInDatabase(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(userRepository.existsByUserNameIgnoreCaseAndStore(user.getUserName(),store)).
                thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()-> userServiceImpl.createUser(userRequestDTO));

        assertEquals("User name is already exist.", exception.getMessage());
        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(userRepository,times(0)).save(user);
    }

    @Test
    void createUserMethodThrowException_emailAlreadyExistsInDatabase(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(userRepository.existsByEmailAndStore(userRequestDTO.getEmail(), store)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userServiceImpl.createUser(userRequestDTO));

        assertEquals("Email already exists in database.", exception.getMessage());

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(userRepository,times(0)).save(user);
    }

    @Test
    void createUserShould_EncodeThePassword_beforeStoringInDatabase() {
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(userRepository.existsByUserNameIgnoreCaseAndStore(userRequestDTO.getUserName(), store)).thenReturn(false);
        when(userRepository.existsByEmailAndStore(userRequestDTO.getEmail(), store)).thenReturn(false);
        when(passwordEncoder.encode(userRequestDTO.getPassword())).thenReturn("Encoded password");

        userServiceImpl.createUser(userRequestDTO);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("Unit_Test", savedUser.getUserName());
        assertEquals("Encoded password", savedUser.getPassword());
        assertNotEquals(userRequestDTO.getPassword(), savedUser.getPassword());
        assertEquals(ADMIN, savedUser.getRole());

        verify(storeAuthorizationService).verifyUserAccess(store);
    }

    @Test
    void getUserById_success(){
        String id = "USER20250909";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(userRepository.findByUserIdAndStore(id,store)).thenReturn(Optional.of(user));

        UserResponseDTO userDetails = userServiceImpl.getUserById(id);

        assertNotNull(userDetails);
        assertEquals(user.getUserName(), userDetails.getUserName());
        assertEquals(user.getEmail(),userDetails.getEmail());

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(userRepository,times(1)).findByUserIdAndStore(id,store);

    }

    @Test
    void  getUserById_shouldThrowException_userNotFoundWithId(){
        String id = "USER20250909";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(userRepository.findByUserIdAndStore(id, store)).thenReturn(Optional.empty());

//        System.out.println("Test-ID: "+id);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                ()-> userServiceImpl.getUserById(id));

        assertEquals("User not found with ID "+id, exception.getMessage());

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(userRepository,times(1)).findByUserIdAndStore(id,store);

    }

    @Test
    void  getUserById_shouldThrowException_IdIsNull(){

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImpl.getUserById(null));

        assertEquals("Entered ID is not valid.",exception.getMessage());
    }

    @Test
    void  getAllUsers_success(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(userRepository.findAllUsersByStore(store)).thenReturn(List.of(user));

        List<UserResponseDTO> allEmployees = userServiceImpl.getAllUsers();

        assertNotNull(allEmployees);
        assertEquals(allEmployees.getFirst().getUserName(), user.getUserName());

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(userRepository,times(1)).findAllUsersByStore(store);
    }

    @Test
    void getAllUsers_shouldReturn_emptyList(){
        when(storeContextService.getCurrentStore()).thenReturn(store);
        when(userRepository.findAllUsersByStore(store)).thenReturn(Collections.emptyList());

        List<UserResponseDTO> allUsers = userServiceImpl.getAllUsers();

        assertEquals(Collections.emptyList(), allUsers);
        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(userRepository,times(1)).findAllUsersByStore(store);
    }

    @Test
    void updateUser_success(){
        UserRequestDTO requestDTO = new UserRequestDTO(
                "new_Name",
                STAFF,
                "newPassword",
                "newemail1@gmail.com",
                "USER");
        String id = "USER20250909";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(userRepository.findByUserIdAndStore(id,store)).thenReturn(Optional.of(user));

        UserResponseDTO updated  = userServiceImpl.updateUser(id,requestDTO);

        assertNotEquals(ADMIN,requestDTO.getRole());
    }

    @Test
    void updateUser_throwsException_whenUserNotFound(){
        UserRequestDTO requestDTO = new UserRequestDTO(
                "new_Name",
                STAFF,
                "newPassword",
                "newemail1@gmail.com",
                "USER");
        String id = "USER20250909";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(userRepository.findByUserIdAndStore(id,store)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                ()-> userServiceImpl.updateUser(id,requestDTO));

        assertEquals("User is not found "+id, exception.getMessage());


    }

    @Test
    void updateUser_onlyUpdate_requiredFields(){
        UserRequestDTO requestDTO =  new UserRequestDTO();
        requestDTO.setUserName("newName");
        requestDTO.setEmail("newemail1@gmail.com");
        String id = "USER20250909";


        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(userRepository.findByUserIdAndStore(id,store)).thenReturn(Optional.of(user));

        UserResponseDTO responseDTO = userServiceImpl.updateUser(id,requestDTO);

        assertEquals(ADMIN, responseDTO.getRole());
        assertEquals("USER20250909",responseDTO.getId());
        assertNotEquals("Unit_Test", responseDTO.getUserName());
    }

    @Test
    void deleteUser_success(){
        String id = "USER20250909";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(userRepository.findByUserIdAndStore(id,store)).thenReturn(Optional.of(user));

        userServiceImpl.deleteUser(id);

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(userRepository,times(1)).delete(user);
    }

    @Test
    void deleteUser_throwsException_whenUserIDNotValid(){
        String id = "USER20250909";
        when(storeContextService.getCurrentStore()).thenReturn(store);
        doNothing().when(storeAuthorizationService).verifyUserAccess(store);
        when(userRepository.findByUserIdAndStore(id,store)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userServiceImpl.deleteUser(id));

        verify(storeAuthorizationService).verifyUserAccess(store);
        verify(userRepository,times(0)).delete(user);

        assertEquals("User is not found "+id,exception.getMessage());

    }

}
