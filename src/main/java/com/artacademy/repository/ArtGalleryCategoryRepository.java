package com.artacademy.repository;

import com.artacademy.entity.ArtGalleryCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtGalleryCategoryRepository extends MongoRepository<ArtGalleryCategory, String> {

    Optional<ArtGalleryCategory> findByName(String name);

    List<ArtGalleryCategory> findByIsActiveTrue();

    List<ArtGalleryCategory> findByParentIdIsNull();
}
