package com.artacademy.repository;

import com.artacademy.entity.ArtGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtGalleryRepository extends JpaRepository<ArtGallery, UUID>, JpaSpecificationExecutor<ArtGallery> {
}
