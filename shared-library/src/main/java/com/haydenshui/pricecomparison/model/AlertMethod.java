package com.haydenshui.pricecomparison.shared.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alert_methods")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertMethod {

    public enum MethodType {
        EMAIL,
        NOTIFICATION,
        NONE;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "favorite_id", nullable = false)
    private Favorite favorite;  // 直接关联 Favorite

    @Column(name = "method_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private MethodType methodType;  // 提醒方式类型

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

