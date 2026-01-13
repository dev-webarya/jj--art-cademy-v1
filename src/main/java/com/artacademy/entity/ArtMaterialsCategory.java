package com.artacademy.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter // Replaced @Data
@Setter // Replaced @Data
@ToString(exclude = { "parent", "subcategories", "artMaterials" }) // Added to break loop
@EqualsAndHashCode(exclude = { "parent", "subcategories", "artMaterials" }) // Added to break loop
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "art_materials_categories")
public class ArtMaterialsCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    // Self-referencing relationship for hierarchy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_materials_parent_id")
    private ArtMaterialsCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<ArtMaterialsCategory> subcategories = new HashSet<>();

    @OneToMany(mappedBy = "artMaterialsCategory", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<ArtMaterials> artMaterials = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}