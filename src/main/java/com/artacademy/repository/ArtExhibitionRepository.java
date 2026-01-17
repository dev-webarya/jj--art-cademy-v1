package com.artacademy.repository;

import com.artacademy.entity.ArtExhibition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ArtExhibitionRepository extends MongoRepository<ArtExhibition, String> {

        List<ArtExhibition> findByDeletedFalse();

        @Query("{'isActive': true, 'deleted': false}")
        List<ArtExhibition> findActiveExhibitions();

        @Query("{'category.categoryId': ?0, 'deleted': false}")
        List<ArtExhibition> findByCategoryId(String categoryId);

        @Query("{'startDate': {$lte: ?0}, 'endDate': {$gte: ?0}, 'deleted': false}")
        List<ArtExhibition> findCurrentExhibitions(LocalDate date);

        @Query("{'startDate': {$gte: ?0}, 'deleted': false}")
        List<ArtExhibition> findUpcomingExhibitions(LocalDate date);
}
