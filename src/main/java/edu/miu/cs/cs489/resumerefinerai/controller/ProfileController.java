package edu.miu.cs.cs489.resumerefinerai.controller;

import edu.miu.cs.cs489.resumerefinerai.dto.request.CreateProfileRequest;
import edu.miu.cs.cs489.resumerefinerai.dto.response.ProfileResponse;
import edu.miu.cs.cs489.resumerefinerai.mapper.ProfileMapper;
import edu.miu.cs.cs489.resumerefinerai.model.Profile;
import edu.miu.cs.cs489.resumerefinerai.service.ProfileService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        ProfileResponse profile = profileService.createProfile(username, request);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Page<ProfileResponse>> getUserProfiles(
            @PathVariable String username,
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sorting criteria (e.g., 'profileName,asc')", example = "profileName,asc")
            @RequestParam(defaultValue = "profileName,asc") String[] sort) {

        // Parse sorting parameters (e.g., "fieldName,direction")
        Sort.Direction direction = sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sorting = Sort.by(direction, sort[0]);

        Pageable pageable = PageRequest.of(page, size, sorting);
        Page<Profile> profilesPage = profileService.getUserProfiles(username, pageable);

        // Map Profile entities to ProfileResponse DTOs
        Page<ProfileResponse> responsePage = profilesPage.map(profileMapper::toDto);
        return ResponseEntity.ok(responsePage);
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
        // Validate file type
        if (file.isEmpty() || !"application/zip".equalsIgnoreCase(file.getContentType())) {
            return ResponseEntity.badRequest().body("Only ZIP files are allowed.");
        }
        // Validate file size (e.g., 5MB max)
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB in bytes
            return ResponseEntity.badRequest().body("File too large (max 5MB).");
        }
        try {
            profileService.extractLatexZip(username, profileName, file);
            return ResponseEntity.ok("Upload and extraction successful.");
        } catch (RuntimeException | IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }



}
