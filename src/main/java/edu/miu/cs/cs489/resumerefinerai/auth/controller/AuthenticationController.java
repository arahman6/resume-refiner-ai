package edu.miu.cs.cs489.resumerefinerai.auth.controller;

import edu.miu.cs.cs489.resumerefinerai.auth.dto.request.AuthenticationRequest;
import edu.miu.cs.cs489.resumerefinerai.auth.dto.request.RegisterRequest;
import edu.miu.cs.cs489.resumerefinerai.auth.dto.response.AuthenticationResponse;
import edu.miu.cs.cs489.resumerefinerai.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    /***
     * for /register
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationResponse);
    }

    /***
     * for /authenticate
     */
    //AuthenticationResponse authenticate(authenticationRequest)
    @PostMapping("/authenticate")
    private ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }
}
