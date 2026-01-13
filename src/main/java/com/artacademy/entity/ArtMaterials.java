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
@Table(name = "art_materials")
// --- FIX: Soft Delete Configuration ---
@SQLDelete(sql = "UPDATE art_materials SET deleted = true WHERE id = ?")
@SQLRestriction("deleted = false")
public class ArtMaterials {

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
    private Integer discount;

    @Column(nullable = false)
    private BigDecimal stock; //count for item

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
    @JoinColumn(name = "art_materials_category_id")
    private ArtMaterialsCategory category;

    @Column(nullable = false)
    private String imageUrl;
}