package com.artacademy.repository;

import com.artacademy.entity.ArtExhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ArtExhibitionRepository extends MongoRepository<ArtExhibition, String> {

        Page<ArtExhibition> findByDeletedFalse(Pageable pageable);

        Optional<ArtExhibition> findByIdAndDeletedFalse(String id);

        Page<ArtExhibition> findByCategoryIdAndDeletedFalse(String categoryId, Pageable pageable);

        Page<ArtExhibition> findByIsActiveTrueAndDeletedFalse(Pageable pageable);

        // Find upcoming exhibitions
        Page<ArtExhibition> findByStartDateAfterAndDeletedFalse(LocalDate date, Pageable pageable);

        // Find active exhibitions
        Page<ArtExhibition> findByStartDateBeforeAndEndDateAfterAndDeletedFalse(LocalDate startBefore,
                        LocalDate endAfter, Pageable pageable);
}
