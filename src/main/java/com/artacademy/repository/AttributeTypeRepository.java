package com.artacademy.repository;

import com.artacademy.entity.AttributeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttributeTypeRepository
                extends JpaRepository<AttributeType, UUID>, JpaSpecificationExecutor<AttributeType> {
}