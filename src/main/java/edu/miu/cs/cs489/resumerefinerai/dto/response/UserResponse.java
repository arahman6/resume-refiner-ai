package edu.miu.cs.cs489.resumerefinerai.dto.response;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String role;
}
