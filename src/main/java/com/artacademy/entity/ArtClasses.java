package com.artacademy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "art_classes")
// --- FIX: Soft Delete Configuration ---
@SQLDelete(sql = "UPDATE art_classes SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class ArtClasses {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(nullable = false)
        private String name;

        @Column(columnDefinition = "TEXT")
        private String description;

        @Column(nullable = false, precision = 10, scale = 2)
        private BigDecimal basePrice;

        @Column(nullable = false)
        @Builder.Default
        private boolean isActive = true;

        @Column(nullable = false)
        private String proficiency; // e.g. Beginner, Intermediate, Advanced

        // --- FIX: Soft Delete Flag ---
        @Column(nullable = false)
        @Builder.Default
        private boolean deleted = false;

        @CreationTimestamp
        private Instant createdAt;

        @UpdateTimestamp
        private Instant updatedAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id")
        private ArtClassesCategory category;

        @Column(nullable = false)
        private String imageUrl;
}