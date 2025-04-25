package edu.miu.cs.cs489.resumerefinerai.service;

import edu.miu.cs.cs489.resumerefinerai.model.Profile;
import edu.miu.cs.cs489.resumerefinerai.dto.request.UpdateResumeRequest;
import edu.miu.cs.cs489.resumerefinerai.repository.ProfileRepository;
import edu.miu.cs.cs489.resumerefinerai.auth.user.User;
import edu.miu.cs.cs489.resumerefinerai.auth.repository.UserRepository;
import edu.miu.cs.cs489.resumerefinerai.util.DockerLatexCompiler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final OpenAIService openAIService;
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public void updateResume(UpdateResumeRequest request) throws IOException {
        // 1. Get user and profile
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Profile profile = profileRepository.findByUserAndProfileName(user, request.getProfileName())
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        String userId = String.valueOf(user.getId());
        String profileName = profile.getProfileName();
        String resumeFileName = user.getFirstName() + " " + user.getLastName() + " Resume.tex";

        // 2. Build paths
        Path templateRoot = Paths.get("src/main/resources/template", userId, profileName);
        Path latexRoot = Paths.get("src/main/resources/latex", userId, profileName);
        Path mainTexFile = latexRoot.resolve(resumeFileName);

        // 3. Clean and copy template to latex
        if (Files.exists(latexRoot)) {
            deleteDirectoryRecursively(latexRoot);
        }
        Files.createDirectories(latexRoot);
        copyDirectoryRecursively(templateRoot, latexRoot);

        // 4. Update selective sections using OpenAI
        List<String> updatableSections = List.of("summary.tex", "skills.tex");  //, "experience.tex"
        Path sectionsDir = latexRoot.resolve("sections");

        for (String section : updatableSections) {
            Path targetFile = sectionsDir.resolve(section);
            if (!Files.exists(targetFile)) {
                throw new RuntimeException("Missing section file: " + targetFile.toAbsolutePath());
            }

            String original = Files.readString(targetFile);
            String updated = openAIService.getUpdatedSection(section, original, request.getJobDescription());
            Files.writeString(targetFile, updated);

            System.out.println("Section updated: " + section);
        }

        // 5. Compile with Docker
        boolean compiled = DockerLatexCompiler.compileWithDocker(mainTexFile);
        if (!compiled) {
            throw new RuntimeException("LaTeX compilation failed.");
        }

        System.out.println("Resume compiled: " + mainTexFile);
    }

    private void deleteDirectoryRecursively(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.deleteIfExists(p);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to delete " + p, e);
                    }
                });
    }

    private void copyDirectoryRecursively(Path source, Path target) throws IOException {
        Files.walk(source).forEach(src -> {
            try {
                Path dest = target.resolve(source.relativize(src));
                if (Files.isDirectory(src)) {
                    Files.createDirectories(dest);
                } else {
                    Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to copy " + src + " to " + target, e);
            }
        });
    }


    public Path getCompiledPdfPath(String userId, String profileName) {
        return Paths.get("src/main/resources/latex", userId, profileName, profileName + ".pdf");
    }

    public Path getPdfPathForUser(String username, String profileName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String fileName = user.getFirstName() + " " + user.getLastName() + " Resume.pdf";
        return Paths.get("src/main/resources/latex", String.valueOf(user.getId()), profileName, fileName);
    }
}
