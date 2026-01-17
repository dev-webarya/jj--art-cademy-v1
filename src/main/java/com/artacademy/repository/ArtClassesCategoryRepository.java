package com.artacademy.repository;

import com.artacademy.entity.ArtClassesCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtClassesCategoryRepository extends MongoRepository<ArtClassesCategory, String> {

    Optional<ArtClassesCategory> findByName(String name);

    List<ArtClassesCategory> findByIsActiveTrue();

    List<ArtClassesCategory> findByParentIdIsNull();
}
