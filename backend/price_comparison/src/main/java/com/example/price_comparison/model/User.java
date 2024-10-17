package com.example.price_comparison.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;


/**
 * User class represents a user in the system.
 */
@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Email(message = "Email should be valid")
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^\\d{13}$", message = "Phone number should be 13 digits")
    private String phone;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // Default role

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Validates the password against the stored encrypted password.
     *
     * @param inputPassword The password to validate.
     * @return true if the password matches, false otherwise.
     */
    public boolean validatePassword(String inputPassword) {
        return this.password.equals(encryptPassword(inputPassword));
    }

    /**
     * Encrypts the provided password using BCrypt.
     *
     * @param password The password to encrypt.
     * @return The encrypted password.
     */
    private String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    /**
     * Prepares the user object before persisting to the database.
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Updates the updatedAt field before updating the user in the database.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
