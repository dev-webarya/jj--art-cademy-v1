package com.artacademy.repository;

import com.artacademy.entity.LmsGalleryItem;
import com.artacademy.enums.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LmsGalleryItemRepository extends MongoRepository<LmsGalleryItem, String> {

    Page<LmsGalleryItem> findByUploadedBy(String userId, Pageable pageable);

    Page<LmsGalleryItem> findByVerificationStatus(VerificationStatus status, Pageable pageable);

    Page<LmsGalleryItem> findByIsPublicTrue(Pageable pageable);

    Page<LmsGalleryItem> findByIsFeaturedTrue(Pageable pageable);

    List<LmsGalleryItem> findByClassId(String classId);

    long countByVerificationStatus(VerificationStatus status);
}
