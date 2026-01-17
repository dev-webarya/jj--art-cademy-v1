package com.artacademy.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "art_classes")
public class ArtClasses {

        @Id
        private String id;

        @Indexed
        private String name;

        private String description;

        private BigDecimal basePrice;

        @Builder.Default
        private boolean isActive = true;

        @Indexed
        private String proficiency; // e.g. Beginner, Intermediate, Advanced

        @Builder.Default
        private boolean deleted = false;

        @CreatedDate
        private Instant createdAt;

        @LastModifiedDate
        private Instant updatedAt;

        // Embedded category reference
        private CategoryRef category;

        private String imageUrl;

        // Embedded images
        @Builder.Default
        private List<ImageRef> images = new ArrayList<>();

        // Schedule info
        private Schedule schedule;

        // Embedded class for category reference
        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CategoryRef {
                private String categoryId;
                private String name;
        }

        // Embedded class for images
        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class ImageRef {
                private String imageUrl;
                private String altText;
                private Integer displayOrder;
                private boolean isPrimary;
        }

        // Embedded class for schedule
        @Getter
        @Setter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Schedule {
                private Integer durationMinutes;
                private Integer maxStudents;
                private List<String> recurringDays;
        }
}