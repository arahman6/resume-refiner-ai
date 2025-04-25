package edu.miu.cs.cs489.resumerefinerai.repository;

import edu.miu.cs.cs489.resumerefinerai.auth.user.Role;
import edu.miu.cs.cs489.resumerefinerai.auth.user.User;
import edu.miu.cs.cs489.resumerefinerai.config.TestConfig;
import edu.miu.cs.cs489.resumerefinerai.model.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestConfig.class)
public class ProfileRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    void findByUser_WithPagination_ReturnsPage() {
        // 1. Create and persist User
        User user = new User("Test", "User", "testuser", "password", Role.MEMBER);
        entityManager.persist(user);

        // 2. Create Profile WITH USER REFERENCE
        Profile profile = new Profile();
        profile.setProfileName("Data Engineer");
        profile.setUser(user); // Explicitly set user
        entityManager.persist(profile);

        // 3. Force flush and clear cache
        entityManager.flush();
        entityManager.clear();

        // 4. Query
        Page<Profile> result = profileRepository.findByUser(
                user,
                PageRequest.of(0, 10)
        );

        assertEquals(1, result.getTotalElements());
    }
}