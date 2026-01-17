package com.artacademy.repository;

import com.artacademy.entity.ArtWorks;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtWorksRepository extends MongoRepository<ArtWorks, String> {

    List<ArtWorks> findByDeletedFalse();

    @Query("{'isActive': true, 'deleted': false}")
    List<ArtWorks> findActiveWorks();

    @Query("{'category.categoryId': ?0, 'deleted': false}")
    List<ArtWorks> findByCategoryId(String categoryId);

    @Query("{'artistName': {$regex: ?0, $options: 'i'}, 'deleted': false}")
    List<ArtWorks> searchByArtist(String artistName);

    @Query("{'name': {$regex: ?0, $options: 'i'}, 'deleted': false}")
    List<ArtWorks> searchByName(String name);
}
