package com.haydenshui.pricecomparison.shared.model;

import com.haydenshui.pricecomparison.shared.util.validation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
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
@Table(
    name = "items",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "sku_id")
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sku_id", nullable = false, columnDefinition = "varchar(63) default ''")
    @NotNull(message = "Platform id cannot be null")
    @NotBlank(message = "Platform id cannot be empty")
    private String skuid;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String name;

    //更改
    @Column(name = "category_id", nullable = false, columnDefinition = "int default 0")
    @NotNull(message = "Category ID cannot be null")
    private int categoryId;
    //

    @Column(nullable = false, length = 15, columnDefinition = "varchar(15) default ''")
    @NotNull(message = "Platform cannot be null")
    @NotBlank(message = "Platform cannot be empty")
    private String platform;

    @Column(nullable = false, columnDefinition = "varchar(2000) default ''")
    @ValidUrl
    private String image;

    @Column(nullable = false, columnDefinition = "varchar(2000) default ''")
    @ValidUrl
    private String url;

    @Column(nullable = false, columnDefinition = "decimal(10,2) default 0.00")
    @NotNull(message = "Price cannot be null")
    private double price;
    
    // Optimistic locking version
    @Version
    private int version;

    @Column(name = "created_at", nullable = false, updatable = false)
    @JsonIgnore
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonIgnore
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
     * Prepares the item object before updating in the database.
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }
}
