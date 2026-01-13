package com.artacademy.repository;

import com.artacademy.entity.ArtExhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtExhibitionRepository
        extends JpaRepository<ArtExhibition, UUID>, JpaSpecificationExecutor<ArtExhibition> {
}
