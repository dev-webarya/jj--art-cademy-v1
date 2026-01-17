package com.artacademy.repository;

import com.artacademy.entity.ArtWorksCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtWorksCategoryRepository extends MongoRepository<ArtWorksCategory, String> {

    @Query("{'parent': null, 'deleted': false}")
    List<ArtWorksCategory> findByParentIsNull();

    boolean existsByName(String name);

    List<ArtWorksCategory> findByDeletedFalse();
}
