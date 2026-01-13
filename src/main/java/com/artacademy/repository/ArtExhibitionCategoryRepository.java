package com.artacademy.repository;

import com.artacademy.entity.ArtExhibitionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtExhibitionCategoryRepository extends JpaRepository<ArtExhibitionCategory, UUID> {
    List<ArtExhibitionCategory> findByParentIsNull();

    boolean existsByName(String name);
}
