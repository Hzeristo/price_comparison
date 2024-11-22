package com.haydenshui.pricecomparison.shared.model;

import com.haydenshui.pricecomparison.shared.util.validation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * User class represents a user in the system.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, columnDefinition = "varchar(63)")
    @ValidUsername
    private String username;

    @Column(nullable = false, columnDefinition = "varchar(255)")
    @ValidPassword
    private String password;

    @Column(unique = true, nullable = false, columnDefinition = "varchar(255)")
    @ValidEmail
    private String email;

    @Column(unique = true, nullable = false, columnDefinition = "varchar(15)")
    @ValidPhone
    private String phone;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, columnDefinition = "varchar(15) default 'USER'")
    private Role role = Role.USER; // Default role
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Prepares the user object before persisting to the database.
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Updates the updatedAt field before updating the user in the database.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }
}
