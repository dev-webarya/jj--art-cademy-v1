package com.artacademy.repository;

import com.artacademy.entity.ArtExhibitionCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtExhibitionCategoryRepository extends MongoRepository<ArtExhibitionCategory, String> {

    Optional<ArtExhibitionCategory> findByName(String name);
}
