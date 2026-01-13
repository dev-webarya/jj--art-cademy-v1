package com.artacademy.repository;

import com.artacademy.entity.ArtMaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtMaterialsRepository
        extends JpaRepository<ArtMaterials, UUID>, JpaSpecificationExecutor<ArtMaterials> {
}
