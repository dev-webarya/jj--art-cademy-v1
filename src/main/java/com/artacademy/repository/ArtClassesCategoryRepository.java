package com.artacademy.repository;

import com.artacademy.entity.ArtClassesCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtClassesCategoryRepository extends MongoRepository<ArtClassesCategory, String> {

    @Query("{'parent': null, 'deleted': false}")
    List<ArtClassesCategory> findByParentIsNull();

    boolean existsByName(String name);

    List<ArtClassesCategory> findByDeletedFalse();

    @Query("{'parent.categoryId': ?0, 'deleted': false}")
    List<ArtClassesCategory> findByParentId(String parentId);
}
