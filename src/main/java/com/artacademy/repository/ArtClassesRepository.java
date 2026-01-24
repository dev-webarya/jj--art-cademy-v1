package com.artacademy.repository;

import com.artacademy.entity.ArtClasses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtClassesRepository extends MongoRepository<ArtClasses, String> {

    Page<ArtClasses> findByDeletedFalse(Pageable pageable);

    Optional<ArtClasses> findByIdAndDeletedFalse(String id);

    Page<ArtClasses> findByCategoryIdAndDeletedFalse(String categoryId, Pageable pageable);

    Page<ArtClasses> findByIsActiveTrueAndDeletedFalse(Pageable pageable);

    // By proficiency level
    Page<ArtClasses> findByProficiencyAndDeletedFalse(String proficiency, Pageable pageable);

    // Text search
    @Query("{ '$text': { '$search': ?0 }, 'deleted': false }")
    List<ArtClasses> searchByText(String searchText);
}
