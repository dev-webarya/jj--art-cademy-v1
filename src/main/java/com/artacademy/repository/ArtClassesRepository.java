package com.artacademy.repository;

import com.artacademy.entity.ArtClasses;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtClassesRepository extends MongoRepository<ArtClasses, String> {

    List<ArtClasses> findByDeletedFalse();

    @Query("{'isActive': true, 'deleted': false}")
    List<ArtClasses> findActiveClasses();

    @Query("{'category.categoryId': ?0, 'deleted': false}")
    List<ArtClasses> findByCategoryId(String categoryId);

    @Query("{'proficiency': ?0, 'deleted': false}")
    List<ArtClasses> findByProficiency(String proficiency);

    @Query("{'name': {$regex: ?0, $options: 'i'}, 'deleted': false}")
    List<ArtClasses> searchByName(String name);
}
