package com.artacademy.repository;

import com.artacademy.entity.ArtWorks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtWorksRepository extends MongoRepository<ArtWorks, String> {

    // Soft delete aware queries
    Page<ArtWorks> findByDeletedFalse(Pageable pageable);

    Optional<ArtWorks> findByIdAndDeletedFalse(String id);

    Page<ArtWorks> findByCategoryIdAndDeletedFalse(String categoryId, Pageable pageable);

    Page<ArtWorks> findByIsActiveTrueAndDeletedFalse(Pageable pageable);

    // Text search
    @Query("{ '$text': { '$search': ?0 }, 'deleted': false }")
    List<ArtWorks> searchByText(String searchText);
}
