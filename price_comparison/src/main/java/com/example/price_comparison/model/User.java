package com.example.price_comparison.model;

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

    @Column(unique = true, nullable = false)
    @NotNull(message = "Username cannot be null")
    @NotBlank(message = "Username cannot be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_]{5,255}$", message = "Username should be between 5 and 255 characters and can only contain letters, numbers, and underscores")
    private String username;

    @Column(nullable = false)
    @NotNull(message = "Password cannot be null")
    @NotBlank(message = "Password cannot be empty")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,255}$", message = "Password should be between 8 and 255 characters and contain at least one uppercase letter, one lowercase letter, and one digit")
    private String password;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Email cannot be null")
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;

    @Column(unique = true, nullable = false)
    @NotNull(message = "Phone cannot be null")
    @NotBlank(message = "Phone cannot be empty")
    @Pattern(regexp = "^\\d{13}$", message = "Phone number should be 13 digits")
    private String phone;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // Default role

    private LocalDateTime createdAt;
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
