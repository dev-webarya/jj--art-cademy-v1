package com.artacademy.repository;

import com.artacademy.entity.ArtMaterials;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtMaterialsRepository extends MongoRepository<ArtMaterials, String> {

        List<ArtMaterials> findByDeletedFalse();

        @Query("{'isActive': true, 'deleted': false}")
        List<ArtMaterials> findActiveMaterials();

        @Query("{'category.categoryId': ?0, 'deleted': false}")
        List<ArtMaterials> findByCategoryId(String categoryId);

        @Query("{'name': {$regex: ?0, $options: 'i'}, 'deleted': false}")
        List<ArtMaterials> searchByName(String name);

        @Query("{'stock': {$gt: 0}, 'deleted': false}")
        List<ArtMaterials> findInStock();
}
