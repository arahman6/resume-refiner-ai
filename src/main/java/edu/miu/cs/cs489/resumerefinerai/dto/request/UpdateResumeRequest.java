package edu.miu.cs.cs489.resumerefinerai.dto.request;

import lombok.Data;

@Data
public class UpdateResumeRequest {
    private String username;
    private String profileName;
    private String jobDescription;
}
