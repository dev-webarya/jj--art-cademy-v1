package com.artacademy.repository;

import com.artacademy.entity.ArtMaterialsCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtMaterialsCategoryRepository extends MongoRepository<ArtMaterialsCategory, String> {

    @Query("{'parent': null, 'deleted': false}")
    List<ArtMaterialsCategory> findByParentIsNull();

    boolean existsByName(String name);

    List<ArtMaterialsCategory> findByDeletedFalse();
}
