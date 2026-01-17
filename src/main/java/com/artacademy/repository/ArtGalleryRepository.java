package com.artacademy.repository;

import com.artacademy.entity.ArtGallery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtGalleryRepository extends MongoRepository<ArtGallery, String> {

    List<ArtGallery> findByDeletedFalse();

    @Query("{'isActive': true, 'deleted': false}")
    List<ArtGallery> findActiveGalleries();

    @Query("{'category.categoryId': ?0, 'deleted': false}")
    List<ArtGallery> findByCategoryId(String categoryId);
}
