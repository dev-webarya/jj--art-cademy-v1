package com.artacademy.repository;

import com.artacademy.entity.ArtWorks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtWorksRepository extends JpaRepository<ArtWorks, UUID>, JpaSpecificationExecutor<ArtWorks> {
}
