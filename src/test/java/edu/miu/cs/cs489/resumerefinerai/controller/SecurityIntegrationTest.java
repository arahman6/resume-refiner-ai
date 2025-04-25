package edu.miu.cs.cs489.resumerefinerai.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.miu.cs.cs489.resumerefinerai.ResumeRefinerAiApplication;
import edu.miu.cs.cs489.resumerefinerai.auth.dto.response.AuthenticationResponse;
import edu.miu.cs.cs489.resumerefinerai.auth.repository.UserRepository;
import edu.miu.cs.cs489.resumerefinerai.auth.user.Role;
import edu.miu.cs.cs489.resumerefinerai.auth.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = ResumeRefinerAiApplication.class)
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;

    @BeforeEach
    void setup() {
        // Create and save test user with encoded password
        testUser = new User("Test", "User", "testuser",
                passwordEncoder.encode("password"), Role.MEMBER);
        userRepository.save(testUser);
    }

    @Test
    void accessSecuredEndpoint_WithValidToken_ReturnsOk() throws Exception {
        // Get valid JWT token
        String token = authenticateAndGetToken("testuser", "password");

        mockMvc.perform(get("/api/v1/profiles/testuser")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    private String authenticateAndGetToken(String username, String password) throws Exception {
        String requestBody = """
            {
                "username": "%s",
                "password": "%s"
            }
            """.formatted(username, password);

        String response = mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return new ObjectMapper().readTree(response).get("token").asText(); // Proper JSON parsing
    }
}