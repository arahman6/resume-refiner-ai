package edu.miu.cs.cs489.resumerefinerai.auth.dto.request;

public record AuthenticationRequest(
        String username,
        String password
) {
}
