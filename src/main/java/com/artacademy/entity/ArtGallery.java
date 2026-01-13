package com.artacademy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "art_galleries")
// --- Soft Delete Configuration ---
@SQLDelete(sql = "UPDATE art_galleries SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class ArtGallery {

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

        @CreationTimestamp
        private Instant createdAt;

        @UpdateTimestamp
        private Instant updatedAt;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "art_gallery_category_id")
        private ArtGalleryCategory artGalleryCategory;

        @Column(nullable = false)
        private String imageUrl;
}