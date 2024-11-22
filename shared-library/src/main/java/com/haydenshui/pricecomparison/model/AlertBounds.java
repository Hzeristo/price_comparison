package com.haydenshui.pricecomparison.shared.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alert_bounds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertBounds {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_id", nullable = false)
    private Favorite favorite;

    @Column(name = "condition_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AlertType conditionType;

    @Column(name = "condition_value", nullable = false)
    private double conditionValue;

    @Column(name = "condition_percent", nullable = false)
    private int conditionPercent;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
    }

}
