package edu.miu.cs.cs489.resumerefinerai.repository;

import edu.miu.cs.cs489.resumerefinerai.model.Profile;
import edu.miu.cs.cs489.resumerefinerai.auth.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUser(User user);
    Optional<Profile> findByUserAndProfileName(User user, String profileName);
    boolean existsByUserAndProfileName(User user, String profileName);

}
