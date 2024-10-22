package com.example.price_comparison.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Item class represents a product in the system.
 */
@Entity
@Table(name = "items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotNull(message = "Name cannot be null")
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column(name = "category_id", nullable = false)
    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;

    @Column(nullable = false, length = 15)
    @NotNull(message = "Platform cannot be null")
    @NotBlank(message = "Platform cannot be empty")
    private String platform;

    @Column(nullable = false)
    @NotNull(message = "Image cannot be null")
    @NotBlank(message = "Image cannot be empty")
    private String image;

    @Column(nullable = false)
    @NotNull(message = "URL cannot be null")
    @NotBlank(message = "URL cannot be empty")
    private String url;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Prepares the item object before persisting to the database.
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * Updates the updatedAt field before updating the item in the database.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }
}
