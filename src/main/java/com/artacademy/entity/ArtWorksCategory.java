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
@ToString(exclude = { "parent", "subcategories", "artWorks" }) // Added to break loop
@EqualsAndHashCode(exclude = { "parent", "subcategories", "artWorks" }) // Added to break loop
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "art_works_categories")
public class ArtWorksCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    // Self-referencing relationship for hierarchy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "art_works_parent_id")
    private ArtWorksCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<ArtWorksCategory> subcategories = new HashSet<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<ArtWorks> artWorks = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}