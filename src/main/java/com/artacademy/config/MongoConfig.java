package com.artacademy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * MongoDB Configuration for ArtAcademy.
 * Enables automatic population of @CreatedDate and @LastModifiedDate fields.
 */
@Configuration
@EnableMongoAuditing
public class MongoConfig {
    // Additional MongoDB configuration can be added here if needed
    // For example, custom converters for BigDecimal or other types
}
