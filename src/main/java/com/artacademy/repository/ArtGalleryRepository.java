package com.artacademy.repository;

import com.artacademy.entity.ArtGallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtGalleryRepository extends MongoRepository<ArtGallery, String> {

    Page<ArtGallery> findByDeletedFalse(Pageable pageable);

    Optional<ArtGallery> findByIdAndDeletedFalse(String id);

    Page<ArtGallery> findByCategoryIdAndDeletedFalse(String categoryId, Pageable pageable);

    Page<ArtGallery> findByIsActiveTrueAndDeletedFalse(Pageable pageable);
}
