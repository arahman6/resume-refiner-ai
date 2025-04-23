package edu.miu.cs.cs489.resumerefinerai.user.repository;

import edu.miu.cs.cs489.resumerefinerai.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
