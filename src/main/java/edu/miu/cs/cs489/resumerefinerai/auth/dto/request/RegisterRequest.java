package edu.miu.cs.cs489.resumerefinerai.auth.dto.request;


import edu.miu.cs.cs489.resumerefinerai.auth.user.Role;

public record RegisterRequest(
        String firstName,
        String lastName,
        String username,
        String password,
        Role role
) {
}
