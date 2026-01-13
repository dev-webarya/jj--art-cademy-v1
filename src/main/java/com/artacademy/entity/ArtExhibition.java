package com.artacademy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "art_exhibitions")
// --- Soft Delete Configuration ---
@SQLDelete(sql = "UPDATE art_exhibitions SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class ArtExhibition {

        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private UUID id;

        @Column(nullable = false)
        private String name;

        @Column(columnDefinition = "TEXT")
        private String description;

        @Column(nullable = false)
        @Builder.Default
        private boolean isActive = true;

        // --- FIX: Soft Delete Flag ---
        @Column(nullable = false)
        @Builder.Default
        private boolean deleted = false;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "art_exhibition_category_id")
        private ArtExhibitionCategory artExhibitionCategory;

        @CreationTimestamp
        private Instant createdAt;

        @UpdateTimestamp
        private Instant updatedAt;

        @Column(nullable = false)
        private String imageUrl;

        @Column(nullable = false)
        private LocalDate startDate;

        @Column(nullable = false)
        private LocalDate endDate;

        @Column(nullable = false)
        private String location;

        @Column(nullable = false)
        private Integer artistCount;

        @Column(nullable = false)
        private Integer ArtworksCount;
}