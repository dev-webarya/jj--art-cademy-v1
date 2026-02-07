package com.artacademy.repository;

import com.artacademy.entity.ArtMaterialsCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtMaterialsCategoryRepository extends MongoRepository<ArtMaterialsCategory, String> {

    Optional<ArtMaterialsCategory> findByName(String name);
}
