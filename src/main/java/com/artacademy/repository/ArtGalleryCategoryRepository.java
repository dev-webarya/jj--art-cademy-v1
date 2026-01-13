package com.artacademy.repository;

import com.artacademy.entity.ArtGalleryCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtGalleryCategoryRepository extends JpaRepository<ArtGalleryCategory, UUID> {
    List<ArtGalleryCategory> findByParentIsNull();

    boolean existsByName(String name);
}
