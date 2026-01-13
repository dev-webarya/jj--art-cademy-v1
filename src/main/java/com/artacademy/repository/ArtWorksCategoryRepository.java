package com.artacademy.repository;

import com.artacademy.entity.ArtWorksCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtWorksCategoryRepository extends JpaRepository<ArtWorksCategory, UUID> {
    List<ArtWorksCategory> findByParentIsNull();

    boolean existsByName(String name);
}
