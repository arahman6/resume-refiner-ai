package edu.miu.cs.cs489.resumerefinerai.service;

import edu.miu.cs.cs489.resumerefinerai.dto.request.CreateProfileRequest;
import edu.miu.cs.cs489.resumerefinerai.dto.response.ProfileResponse;
import edu.miu.cs.cs489.resumerefinerai.exception.DuplicateProfileException;
import edu.miu.cs.cs489.resumerefinerai.mapper.ProfileMapper;
import edu.miu.cs.cs489.resumerefinerai.model.Profile;
import edu.miu.cs.cs489.resumerefinerai.repository.ProfileRepository;
import edu.miu.cs.cs489.resumerefinerai.auth.user.User;
import edu.miu.cs.cs489.resumerefinerai.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper;


    public ProfileResponse createProfile(String username, CreateProfileRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean exists = profileRepository.existsByUserAndProfileName(user, request.getProfileName());
        if (exists) {
            throw new DuplicateProfileException("Profile name already exists for this user.");
        }


        Profile profile = profileMapper.toEntity(request, user);
        Profile savedProfile = profileRepository.save(profile);
        createLatexFolderStructure(user, savedProfile.getProfileName());
        return profileMapper.toDto(savedProfile);
    }


    public Page<Profile> getUserProfiles(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return profileRepository.findByUser(user, pageable);
    }

    public Profile getProfile(String username, String profileName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return profileRepository.findByUserAndProfileName(user, profileName)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    private void createLatexFolderStructure(User user, String profileName) {
        String userId = String.valueOf(user.getId());
        Path baseDir = Paths.get("src/main/resources/latex", userId, profileName);
        Path sectionsDir = baseDir.resolve("sections");

        try {
            // 💣 Delete old folder if it exists
            if (Files.exists(baseDir)) {
                deleteDirectoryRecursively(baseDir);
            }

            Files.createDirectories(sectionsDir);

            // Create empty .tex files
            List<String> sectionFiles = List.of(
                    "summary.tex", "skills.tex", "experience.tex",
                    "projects.tex", "educations.tex", "publications.tex"
            );
            for (String filename : sectionFiles) {
                Files.createFile(sectionsDir.resolve(filename));
            }

            Files.createFile(baseDir.resolve("header.tex"));
            Files.createFile(baseDir.resolve(profileName + ".tex"));

        } catch (IOException e) {
            throw new RuntimeException("Could not create LaTeX folder structure", e);
        }
    }

    private void deleteDirectoryRecursively(Path path) throws IOException {
        if (Files.notExists(path)) return;

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

    public void extractLatexZip(String username, String profileName, MultipartFile zipFile) throws IOException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUserAndProfileName(user, profileName)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        Path extractTo = Paths.get("src/main/resources/template", String.valueOf(user.getId()), profileName);
        Files.createDirectories(extractTo);

        Path tempZip = Files.createTempFile("upload-", ".zip");
        zipFile.transferTo(tempZip.toFile());

        System.out.println("TEMP ZIP PATH: " + tempZip);
        System.out.println("Exists? " + Files.exists(tempZip));
        System.out.println("Size: " + Files.size(tempZip));

        try (FileSystem zipFs = FileSystems.newFileSystem(tempZip, (ClassLoader) null)) {
            for (Path root : zipFs.getRootDirectories()) {
                Files.walk(root)
                        .filter(Files::isRegularFile)
                        .forEach(zipEntry -> {
                            try {
                                Path relativePath = root.relativize(zipEntry);
                                Path target = extractTo.resolve(relativePath.toString());
                                Files.createDirectories(target.getParent());
                                Files.copy(zipEntry, target, StandardCopyOption.REPLACE_EXISTING);
                            } catch (IOException e) {
                                throw new RuntimeException("Failed to extract: " + zipEntry, e);
                            }
                        });
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to open zip file: " + tempZip, e);
        } finally {
            Files.deleteIfExists(tempZip);
        }
    }

}