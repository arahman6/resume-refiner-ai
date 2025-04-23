package edu.miu.cs.cs489.resumerefinerai.auth;


import edu.miu.cs.cs489.resumerefinerai.user.Role;

public record RegisterRequest(
        String firstName,
        String lastName,
        String username,
        String password,
        Role role
) {
}
