package edu.miu.cs.cs489.resumerefinerai.repository;

import edu.miu.cs.cs489.resumerefinerai.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    // Optional: Add custom query methods if needed later
}