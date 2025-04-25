package edu.miu.cs.cs489.resumerefinerai.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class CreateProfileRequest {
    @NotBlank
    private String profileName;

    @NotBlank
    private String profileDescription;
}