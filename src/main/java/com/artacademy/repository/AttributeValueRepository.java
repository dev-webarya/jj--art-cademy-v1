package com.artacademy.repository;

import com.artacademy.entity.AttributeValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttributeValueRepository
                extends JpaRepository<AttributeValue, UUID>, JpaSpecificationExecutor<AttributeValue> {
}