package com.firstproject.smartinventory.IntegrationTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.UserRequestDTO;
import com.sun.jdi.event.ExceptionEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static com.firstproject.smartinventory.entity.Role.STAFF;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class UserControllerIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequestDTO validUser, invalidUser;
    private String token;

    @BeforeEach
    void setUp(){
        validUser = new UserRequestDTO();
        validUser.setUserName("testAddUser");
        validUser.setRole(STAFF);
        validUser.setPassword("testStaff");
        validUser.setEmail("testUser1@email.com");

        invalidUser = new UserRequestDTO();
        invalidUser.setUserName("te");
        invalidUser.setRole(STAFF);
        invalidUser.setPassword("test");
        invalidUser.setEmail("testUser2@email.com");
    }

    @Test
    void createUser_success() throws Exception {
        String token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(post("/users")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void createUser_throwsException_whenValidationFails() throws Exception{
        token = getJwtToken("test1@email.com","testPass");

        mockMvc.perform(post("/users")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    void createUser_throwsException_whenAdminAccessTheEndPoint() throws Exception{
        token = super.getJwtToken("testAdmin1@email.com","testAdminPass");

        mockMvc.perform(post("/users")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    void createUser_throwsException_whenStaffAccessTheEndPoint() throws Exception{
        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(post("/users")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    void getAllUsers_success() throws Exception{

        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/users")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].userName").value(containsInAnyOrder("testUser","testAdmin","testStaff")))
                .andReturn();
    }

    @Test
    void getAllUser_throwsException_adminAccessTheEndPoint() throws Exception{

        token = super.getJwtToken("testAdmin1@email.com","testAdminPass");

        mockMvc.perform(get("/users")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isForbidden())
                .andReturn();
    }
    @Test
    void getAllUser_throwsException_staffAccessTheEndPoint() throws Exception{

        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(get("/users")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void getUserById_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        MvcResult result = mockMvc.perform(post("/users")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser)))
                        .andExpect(status().isOk())
                                .andReturn();
        String response = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        String id = node.get("id").asText();

        mockMvc.perform(get("/users/id/"+id)
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testAddUser"))
                .andReturn();
    }

    @Test
    void getUserById_throwsException_whenIdIsInValid() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/users/id/invalid")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void getUserById_throwsException_whenAdminAccessTheEndPoint() throws Exception{

        token = super.getJwtToken("test1@email.com","testPass");
        String admin = super.getJwtToken("testAdmin1@email.com","testAdminPass");


        MvcResult result = mockMvc.perform(post("/users")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        String id = node.get("id").asText();

        mockMvc.perform(get("/users/id/"+id)
                        .header("Authorization",admin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void getUserById_throwsException_whenStaffAccessTheEndPoint() throws Exception{

        token = super.getJwtToken("test1@email.com","testPass");
        String staff = super.getJwtToken("testAdmin1@email.com","testAdminPass");


        MvcResult result = mockMvc.perform(post("/users")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isOk())
                .andReturn();
        String response = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        String id = node.get("id").asText();

        mockMvc.perform(get("/users/id/"+id)
                        .header("Authorization",staff)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void getUserByName_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        MvcResult result = mockMvc.perform(post("/users")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser)))
                        .andExpect(status().isOk())
                                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        String userName = node.get("userName").asText();

        mockMvc.perform(get("/users/"+userName)
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("testUser1@email.com"))
                .andReturn();
    }
    @Test
    void getUserByUserName_throwsException_whenUsernameIsInvalid() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(get("/users/invalid")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void getUserByUserName_throwsException_whenAdminAccessTheEndPoint() throws Exception{
        token = super.getJwtToken("testAdmin1@email.com","testAdminPass");

        mockMvc.perform(get("/users/username")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    void getUserByUserName_throwsException_whenStaffAccessTheEndPoint() throws Exception{
        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(get("/users/username")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isForbidden())
                .andReturn();

    }

    @Test
    void updateUser_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(put("/users/USER-6E62B269")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("testAddUser"))
                .andReturn();
    }

    @Test
    void updateUser_throwsException_whenUserIdIsInvalid() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(put("/users/invalid")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void updateUser_throwsException_whenAdminAccessTheEndPoint() throws Exception{
        token = super.getJwtToken("testAdmin1@email.com","testAdminPass");

        mockMvc.perform(put("/users/USER-6E62B269")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isForbidden())
                .andReturn();
    }


    @Test
    void updateUser_throwsException_whenStaffAccessTheEndPoint() throws Exception{
        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(put("/users/USER-6E62B269")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUser)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void deleteUser_success() throws Exception {
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(delete("/users/USER-6E62B269")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void deleteUser_throwsException_whenIdIsInvalid() throws Exception {
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(delete("/users/invalid")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void deleteUser_throwsException_whenAdminAccessTheEndPoint() throws Exception {
        token = super.getJwtToken("testAdmin1@email.com","testAdminPass");

        mockMvc.perform(delete("/users/USER-6E62B269")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void deleteUser_throwsException_whenStaffAccessTheEndPoint() throws Exception {
        token = super.getJwtToken("testStaff1@email.com","testStaffPass");

        mockMvc.perform(delete("/users/USER-6E62B269")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
