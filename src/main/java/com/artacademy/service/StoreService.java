package com.artacademy.service;

import com.artacademy.dto.request.StoreRequestDto;
import com.artacademy.dto.response.StoreResponseDto;
import com.artacademy.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public interface StoreService {

    StoreResponseDto createStore(StoreRequestDto requestDto);

    Page<StoreResponseDto> getAllStores(Specification<Store> spec, Pageable pageable);

    StoreResponseDto getStoreById(UUID id);

    StoreResponseDto updateStore(UUID id, StoreRequestDto requestDto);

    void deleteStore(UUID id);
}