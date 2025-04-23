package edu.miu.cs.cs489.resumerefinerai.auth;

public record AuthenticationRequest(
        String username,
        String password
) {
}
