package com.artacademy.service;

import com.artacademy.dto.request.StockItemRequestDto;
import com.artacademy.dto.response.StockItemResponseDto;
import com.artacademy.entity.StockItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

public interface StockItemService {

    StockItemResponseDto createStockItem(StockItemRequestDto requestDto);

    Page<StockItemResponseDto> getAllStockItems(Specification<StockItem> spec, Pageable pageable);

    StockItemResponseDto getStockItemById(UUID id);

    StockItemResponseDto updateStockItem(UUID id, StockItemRequestDto requestDto);

    void deleteStockItem(UUID id);
}