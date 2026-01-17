package com.artacademy.repository;

import com.artacademy.entity.ArtMaterials;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtMaterialsRepository extends MongoRepository<ArtMaterials, String> {

        // Soft delete aware queries
        Page<ArtMaterials> findByDeletedFalse(Pageable pageable);

        Optional<ArtMaterials> findByIdAndDeletedFalse(String id);

        Page<ArtMaterials> findByCategoryIdAndDeletedFalse(String categoryId, Pageable pageable);

        Page<ArtMaterials> findByIsActiveTrueAndDeletedFalse(Pageable pageable);

        // Text search
        @Query("{ '$text': { '$search': ?0 }, 'deleted': false }")
        List<ArtMaterials> searchByText(String searchText);
}
