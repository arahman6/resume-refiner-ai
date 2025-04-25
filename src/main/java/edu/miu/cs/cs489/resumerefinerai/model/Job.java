package edu.miu.cs.cs489.resumerefinerai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobDescription;

}
