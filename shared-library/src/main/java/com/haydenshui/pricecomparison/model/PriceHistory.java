package com.haydenshui.pricecomparison.shared.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "price_history")
@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "item_id", nullable = false)
    private int itemId;  
    
    @Column(name = "platform", nullable = false)
    @NotNull(message = "Platform cannot be null")
    @NotBlank(message = "Platform cannot be empty")
    private String platform;
    
    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime timestamp;

    /**
     * Prepares the user object before persisting to the database.
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        this.timestamp = now;
    }
}
