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
@ToString(exclude = { "parent", "subcategories", "artExhibition" }) // Added to break loop
@EqualsAndHashCode(exclude = { "parent", "subcategories", "artExhibition" }) // Added to break loop
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exhibition_categories")
public class ArtExhibitionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    // Self-referencing relationship for hierarchy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_parent_id")
    private ArtExhibitionCategory parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<ArtExhibitionCategory> subcategories = new HashSet<>();

    @OneToMany(mappedBy = "artExhibitionCategory", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<ArtExhibition> artExhibition = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}