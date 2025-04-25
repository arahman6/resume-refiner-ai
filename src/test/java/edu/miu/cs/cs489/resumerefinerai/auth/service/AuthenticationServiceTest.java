package edu.miu.cs.cs489.resumerefinerai.auth.service;

import edu.miu.cs.cs489.resumerefinerai.auth.dto.request.AuthenticationRequest;
import edu.miu.cs.cs489.resumerefinerai.auth.dto.request.RegisterRequest;
import edu.miu.cs.cs489.resumerefinerai.auth.dto.response.AuthenticationResponse;
import edu.miu.cs.cs489.resumerefinerai.auth.repository.UserRepository;
import edu.miu.cs.cs489.resumerefinerai.auth.user.Role;
import edu.miu.cs.cs489.resumerefinerai.auth.user.User;
import edu.miu.cs.cs489.resumerefinerai.config.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock(strictness = Mock.Strictness.LENIENT)
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;



    @Test
    void register_NewUser_ReturnsToken() {
        // Mock User object properly
        User mockUser = new User("Arif", "Rahman", "arif", "encodedPassword", Role.MEMBER);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        when(jwtService.generateToken(mockUser)).thenReturn("jwt-token");


        // Arrange
        RegisterRequest request = new RegisterRequest("Arif", "Rahman", "arif", "password", Role.MEMBER);
        when(userRepository.findByUsername("arif")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("jwt-token");

        // Act
        AuthenticationResponse response = authenticationService.register(request);

        // Assert
        assertEquals("jwt-token", response.token());
    }
}