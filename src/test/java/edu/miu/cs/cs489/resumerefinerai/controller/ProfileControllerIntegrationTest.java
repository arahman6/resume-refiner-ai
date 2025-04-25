package edu.miu.cs.cs489.resumerefinerai.controller;

import edu.miu.cs.cs489.resumerefinerai.ResumeRefinerAiApplication;
import edu.miu.cs.cs489.resumerefinerai.auth.user.Role;
import edu.miu.cs.cs489.resumerefinerai.dto.request.CreateProfileRequest;
import edu.miu.cs.cs489.resumerefinerai.model.Profile;
import edu.miu.cs.cs489.resumerefinerai.repository.ProfileRepository;
import edu.miu.cs.cs489.resumerefinerai.auth.repository.UserRepository;
import edu.miu.cs.cs489.resumerefinerai.auth.user.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ResumeRefinerAiApplication.class)
@AutoConfigureMockMvc
class ProfileControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRepository profileRepository;

    private User testUser;

    @BeforeEach
    void setup() {
        testUser = userRepository.save(new User("Test", "User", "testuser", "password", Role.MEMBER));
    }

    @AfterEach
    void tearDown() {
        profileRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createProfile_ValidRequest_ReturnsCreated() throws Exception {
        // Arrange
        String requestBody = """
            {
                "profileName": "Data Engineer",
                "profileDescription": "Builds data pipelines"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/api/v1/profiles/testuser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profileName").value("Data Engineer"));
    }
}