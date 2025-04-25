package edu.miu.cs.cs489.resumerefinerai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CreateProfileRequest {
    @NotBlank
    private String profileName;

    @NotBlank
    private String profileDescription;
}