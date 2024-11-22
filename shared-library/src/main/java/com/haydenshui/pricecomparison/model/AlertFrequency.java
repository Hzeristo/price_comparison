package com.haydenshui.pricecomparison.shared.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "alert_frequencies")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertFrequency {
    
    public enum FrequencyType {
        DAILY,       
        WEEKLY,      
        MONTHLY,    
        HOURLY,      
        CUSTOM
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_id", nullable = false)
    private Favorite favorite;  

    @Column(name = "frequency_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FrequencyType frequencyType; 

    @Column(name = "frequency_value", nullable = false)
    private int frequencyValue;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}


