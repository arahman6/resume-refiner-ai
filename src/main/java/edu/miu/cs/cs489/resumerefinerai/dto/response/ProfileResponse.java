package edu.miu.cs.cs489.resumerefinerai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
    private Long id;
    private String profileName;
    private String profileDescription;
    private String username;
}
