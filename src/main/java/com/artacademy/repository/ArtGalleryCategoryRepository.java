package com.artacademy.repository;

import com.artacademy.entity.ArtGalleryCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtGalleryCategoryRepository extends MongoRepository<ArtGalleryCategory, String> {

    @Query("{'parent': null, 'deleted': false}")
    List<ArtGalleryCategory> findByParentIsNull();

    boolean existsByName(String name);

    List<ArtGalleryCategory> findByDeletedFalse();
}
