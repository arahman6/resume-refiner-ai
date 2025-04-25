package edu.miu.cs.cs489.resumerefinerai.service;

import edu.miu.cs.cs489.resumerefinerai.auth.user.Role;
import edu.miu.cs.cs489.resumerefinerai.dto.request.CreateProfileRequest;
import edu.miu.cs.cs489.resumerefinerai.dto.response.ProfileResponse;
import edu.miu.cs.cs489.resumerefinerai.mapper.ProfileMapper;
import edu.miu.cs.cs489.resumerefinerai.model.Profile;
import edu.miu.cs.cs489.resumerefinerai.repository.ProfileRepository;
import edu.miu.cs.cs489.resumerefinerai.auth.user.User;
import edu.miu.cs.cs489.resumerefinerai.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileMapper profileMapper;

    @InjectMocks
    private ProfileService profileService;

    private CreateProfileRequest request;
    private ProfileResponse mockResponse;

    private CreateProfileRequest validRequest;

    @BeforeEach
    void setup() {
        // Initialize valid request
        validRequest = new CreateProfileRequest("Dev", "Description");

        // Configure mapper
        User mockUser = new User("Test", "User", "testuser", "pass", Role.MEMBER);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

    }

    @Test
    void createProfile_Success() {
        // Arrange
        User mockUser = new User("Test", "User", "testuser", "pass", Role.MEMBER);
        CreateProfileRequest validRequest = new CreateProfileRequest("Dev", "Description");
        Profile mockProfile = new Profile(1L, "Dev", "Description", mockUser);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(profileRepository.existsByUserAndProfileName(mockUser, "Dev")).thenReturn(false);
        when(profileMapper.toEntity(validRequest, mockUser)).thenReturn(mockProfile);
        when(profileRepository.save(mockProfile)).thenReturn(mockProfile);
        when(profileMapper.toDto(mockProfile)).thenReturn(new ProfileResponse(1L, "Dev", "Description", "testuser"));

        // Act
        ProfileResponse response = profileService.createProfile("testuser", validRequest);

        // Assert
        assertNotNull(response);
        assertEquals("Dev", response.getProfileName());
        assertEquals("testuser", response.getUsername());
    }

}