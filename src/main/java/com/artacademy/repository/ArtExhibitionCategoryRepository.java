package com.artacademy.repository;

import com.artacademy.entity.ArtExhibitionCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtExhibitionCategoryRepository extends MongoRepository<ArtExhibitionCategory, String> {

    @Query("{'parent': null, 'deleted': false}")
    List<ArtExhibitionCategory> findByParentIsNull();

    boolean existsByName(String name);

    List<ArtExhibitionCategory> findByDeletedFalse();
}
