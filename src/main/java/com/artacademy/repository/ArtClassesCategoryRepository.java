package com.artacademy.repository;

import com.artacademy.entity.ArtClassesCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtClassesCategoryRepository extends JpaRepository<ArtClassesCategory, UUID> {
    List<ArtClassesCategory> findByParentIsNull();

    boolean existsByName(String name);
}
