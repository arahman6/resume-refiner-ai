package edu.miu.cs.cs489.resumerefinerai.model;

import edu.miu.cs.cs489.resumerefinerai.auth.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "profiles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "profile_name"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_name")
    private String profileName;

    private String profileDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
