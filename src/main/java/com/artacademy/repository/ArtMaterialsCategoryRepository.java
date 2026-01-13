package com.artacademy.repository;

import com.artacademy.entity.ArtMaterialsCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtMaterialsCategoryRepository extends JpaRepository<ArtMaterialsCategory, UUID> {
    List<ArtMaterialsCategory> findByParentIsNull();

    boolean existsByName(String name);
}
