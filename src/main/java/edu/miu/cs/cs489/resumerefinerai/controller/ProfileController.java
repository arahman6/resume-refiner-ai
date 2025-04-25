package edu.miu.cs.cs489.resumerefinerai.controller;

import edu.miu.cs.cs489.resumerefinerai.dto.request.CreateProfileRequest;
import edu.miu.cs.cs489.resumerefinerai.dto.response.ProfileResponse;
import edu.miu.cs.cs489.resumerefinerai.mapper.ProfileMapper;
import edu.miu.cs.cs489.resumerefinerai.model.Profile;
import edu.miu.cs.cs489.resumerefinerai.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    @PostMapping("/{username}")
    public ResponseEntity<ProfileResponse> createProfile(
            @PathVariable String username,
            @Valid @RequestBody CreateProfileRequest request
    ) {
        Profile profile = profileService.createProfile(username, request);
        return ResponseEntity.ok(profileMapper.toDto(profile));
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Profile>> getUserProfiles(@PathVariable String username) {
        return ResponseEntity.ok(profileService.getUserProfiles(username));
    }

    @GetMapping("/{username}/{profileName}")
    public ResponseEntity<Profile> getSingleProfile(
            @PathVariable String username,
            @PathVariable String profileName) {
        return ResponseEntity.ok(profileService.getProfile(username, profileName));
    }


    @PostMapping("/{username}/{profileName}/upload")
    public ResponseEntity<String> uploadLatexZip(
            @PathVariable String username,
            @PathVariable String profileName,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            profileService.extractLatexZip(username, profileName, file);
            return ResponseEntity.ok("Upload and extraction successful.");
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }


}
