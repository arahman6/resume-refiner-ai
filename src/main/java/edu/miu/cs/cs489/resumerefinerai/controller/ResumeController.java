package edu.miu.cs.cs489.resumerefinerai.controller;

import edu.miu.cs.cs489.resumerefinerai.dto.request.UpdateResumeRequest;
import edu.miu.cs.cs489.resumerefinerai.service.ResumeService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/v1/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateResume(@RequestBody UpdateResumeRequest request) {
        try {
            resumeService.updateResume(request);
            return ResponseEntity.ok("Resume updated and compiled successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/download/{username}/{profileName}")
    public ResponseEntity<FileSystemResource> downloadResume(
            @PathVariable String username,
            @PathVariable String profileName
    ) {
        Path pdfPath = resumeService.getPdfPathForUser(username, profileName);
        File pdf = pdfPath.toFile();

        if (!pdf.exists()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + profileName + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new FileSystemResource(pdf));
    }

}
