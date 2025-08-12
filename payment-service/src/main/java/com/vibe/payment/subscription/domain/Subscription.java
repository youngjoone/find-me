package com.vibe.payment.subscription.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private String plan; // e.g., "free", "premium"

    @Column(nullable = false)
    private String status; // e.g., "active", "inactive", "cancelled"

    @Column(nullable = false)
    private LocalDateTime currentPeriodEnd;
}
