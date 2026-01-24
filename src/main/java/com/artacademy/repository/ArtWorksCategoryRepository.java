package com.artacademy.repository;

import com.artacademy.entity.ArtWorksCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtWorksCategoryRepository extends MongoRepository<ArtWorksCategory, String> {

    Optional<ArtWorksCategory> findByName(String name);

    List<ArtWorksCategory> findByIsActiveTrue();

    List<ArtWorksCategory> findByParentIdIsNull();
}
