package com.firstproject.smartinventory.IntegrationTest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.firstproject.smartinventory.dto.CategoriesDTO;
import org.h2.command.dml.MergeUsing;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class
CategoriesControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;


    private CategoriesDTO valid, invalid, valid2;
    private String token;

    @BeforeEach
    void setUp() {
        valid = new CategoriesDTO();
        valid.setName("Laptops");

        invalid = new CategoriesDTO();
        invalid.setName("");

        valid2 = new CategoriesDTO();
        valid2.setName("Mobiles");
    }

    @Test
    void createCategories_success() throws Exception {
        token = super.getJwtToken("test1@email.com", "testPass");

        mockMvc.perform(post("/categories")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valid)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptops"))
                .andReturn();
    }

    @Test
    void createCategories_throwsException_whenNameContainsLessThanThreeCharacters() throws Exception {
        token = super.getJwtToken("test1@email.com", "testPass");
        mockMvc.perform(post("/categories")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void createCategories_throwsExceptionWhenUserIsNotAuthorized() throws Exception {
        token = super.getJwtToken("testStaff1@email.com", "testStaff");

        mockMvc.perform(post("/categories")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valid)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void getAllCategories_success() throws Exception {
        token = super.getJwtToken("test1@email.com", "testPass");

        mockMvc.perform(post("/categories")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valid)))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(post("/categories")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valid2)))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(get("/categories")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].name").value(containsInAnyOrder("Mobiles","Laptops","Laptop's")))

                .andReturn();
    }

    @Test
    void getAllCategories_throwsException_whenTokenNotValid() throws Exception{
        String token = "NoAuth";
        mockMvc.perform(get("/categories")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void deleteCategories_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        MvcResult result =  mockMvc.perform(post("/categories")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valid2)))
                .andExpect(status().isOk())
                .andReturn();


        String response = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        String id = node.get("id").asText();

        mockMvc.perform(delete("/categories/delete/"+id)
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(valid2)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void deleteCategories_throwsException_whenUserHasNoAuthorization() throws  Exception {

        token = super.getJwtToken("test1@email.com","testPass");

         String tokenStaff = super.getJwtToken("testStaff1@email.com","testStaff");

        MvcResult result =  mockMvc.perform(post("/categories")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valid2)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        String id = node.get("id").asText();

        mockMvc.perform(delete("/categories/delete/"+id)
                .header("Authorization",tokenStaff)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void deleteCategories_throwsException_whenCategoriesNotFoundWithID() throws Exception {
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(delete("/categories/delete/invalid")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

    }

    @Test
    void updateCategories_success() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

         MvcResult result =  mockMvc.perform(post("/categories")
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(valid2)))
                        .andExpect(status().isOk())
                                .andReturn();

         String response = result.getResponse().getContentAsString();
         JsonNode node = objectMapper.readTree(response);
         String id = node.get("id").asText();
        mockMvc.perform(put("/categories/"+id)
                .header("Authorization",token)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valid)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptops"))
                .andReturn();
    }

    @Test
    void updateCategories_throwsException_whenCategoriesIdIsInvalid() throws Exception{
        token = super.getJwtToken("test1@email.com","testPass");

        mockMvc.perform(put("/categories/invalid")
                        .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(valid2)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void updateCategories_throwsException_whenUserIsNotAuthorized() throws  Exception{
        token = super.getJwtToken("test1@email.com","testPass");
        String staff = super.getJwtToken("testStaff1@email.com","testStaff");

       MvcResult result =  mockMvc.perform(post("/categories")
                .header("Authorization", token)
                .contentType( MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(valid)))
                        .andExpect(status().isOk())
                                .andReturn();

       String response = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        String id = node.get("id").asText();


        mockMvc.perform(put("/categories/"+id)
                .header("Authorization",staff)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(valid2)))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void updateCategories_throwsException_whenNoAuthToken() throws Exception{

        token = super.getJwtToken("test1@email.com","testPass");
        String noAuthToken = "noAuth";

        MvcResult result =  mockMvc.perform(post("/categories")
                        .header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(valid2)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        JsonNode node = objectMapper.readTree(response);
        String id = node.get("id").asText();

        mockMvc.perform(put("/categories/"+id)
                .header("Authorization",noAuthToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(valid)))
                .andExpect(status().isForbidden())
                .andReturn();
    }
}
