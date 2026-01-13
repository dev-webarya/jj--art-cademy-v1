package com.artacademy.repository;

import com.artacademy.entity.ArtClasses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtClassesRepository extends JpaRepository<ArtClasses, UUID>, JpaSpecificationExecutor<ArtClasses> {
}
