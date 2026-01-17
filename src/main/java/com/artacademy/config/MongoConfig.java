package com.artacademy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.artacademy.repository")
public class MongoConfig {
    // MongoDB auditing enabled for @CreatedDate and @LastModifiedDate annotations
}
