package com.artacademy.service.impl;

import com.artacademy.dto.request.StockItemRequestDto;
import com.artacademy.dto.response.StockItemResponseDto;
import com.artacademy.entity.Product;
import com.artacademy.entity.StockItem;
import com.artacademy.entity.Store;
import com.artacademy.entity.User;
import com.artacademy.exception.InvalidRequestException;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.StockItemMapper;
import com.artacademy.repository.ProductRepository;
import com.artacademy.repository.StockItemRepository;
import com.artacademy.repository.StoreRepository;
import com.artacademy.repository.UserRepository;
import com.artacademy.service.StockItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockItemServiceImpl implements StockItemService {

    private final StockItemRepository stockItemRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final StockItemMapper stockItemMapper;

    @Override
    @Transactional
    public StockItemResponseDto createStockItem(StockItemRequestDto requestDto) {
        log.info("Creating stock item for product: {} at store: {}", requestDto.getProductId(),
                requestDto.getStoreId());
        validateStoreAccess(requestDto.getStoreId());

        // 2. Existence Check
        Optional<StockItem> existing;
        if (requestDto.getStoreId() == null) {
            existing = stockItemRepository.findByProductIdAndStoreIdIsNull(requestDto.getProductId());
        } else {
            existing = stockItemRepository.findByProductIdAndStoreId(requestDto.getProductId(),
                    requestDto.getStoreId());
        }

        if (existing.isPresent()) {
            throw new DataIntegrityViolationException("StockItem already exists. Use PUT to update quantity.");
        }

        StockItem stockItem = stockItemMapper.toEntity(requestDto);

        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", requestDto.getProductId()));
        stockItem.setProduct(product);

        if (requestDto.getStoreId() != null) {
            Store store = storeRepository.findById(requestDto.getStoreId())
                    .orElseThrow(() -> new ResourceNotFoundException("Store", "id", requestDto.getStoreId()));
            stockItem.setStore(store);
        }

        return stockItemMapper.toDto(stockItemRepository.save(stockItem));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StockItemResponseDto> getAllStockItems(Specification<StockItem> spec, Pageable pageable) {
        // Future enhancement: Force filter by store if user is StoreManager
        return stockItemRepository.findAll(spec, pageable).map(stockItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public StockItemResponseDto getStockItemById(UUID id) {
        return stockItemRepository.findById(id)
                .map(stockItemMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("StockItem", "id", id));
    }

    @Override
    @Transactional
    public StockItemResponseDto updateStockItem(UUID id, StockItemRequestDto requestDto) {
        StockItem existingItem = stockItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockItem", "id", id));

        // 1. Security Check: Ensure Manager owns this stock item's store
        UUID storeIdToCheck = (existingItem.getStore() != null) ? existingItem.getStore().getId() : null;
        validateStoreAccess(storeIdToCheck);

        stockItemMapper.updateEntityFromDto(requestDto, existingItem);
        return stockItemMapper.toDto(stockItemRepository.save(existingItem));
    }

    @Override
    @Transactional
    public void deleteStockItem(UUID id) {
        log.warn("Deleting stock item ID: {}", id);
        StockItem stockItem = stockItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StockItem", "id", id));

        UUID storeIdToCheck = (stockItem.getStore() != null) ? stockItem.getStore().getId() : null;
        validateStoreAccess(storeIdToCheck);

        stockItemRepository.delete(stockItem);
    }

    /**
     * Critical Security Logic:
     * If current user is a STORE_MANAGER, they can only operate on the store ID
     * assigned to their profile.
     * Admin can operate on any store.
     */
    private void validateStoreAccess(UUID targetStoreId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new AccessDeniedException("User not found"));

        boolean isAdmin = currentUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"));
        boolean isManager = currentUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_STORE_MANAGER"));

        if (isAdmin)
            return; // Admins can do anything

        if (isManager) {
            if (currentUser.getManagedStore() == null) {
                throw new InvalidRequestException("You are a Manager but not assigned to any Store!");
            }
            if (targetStoreId == null) {
                throw new AccessDeniedException("Managers cannot access Central Warehouse stock.");
            }
            if (!currentUser.getManagedStore().getId().equals(targetStoreId)) {
                throw new AccessDeniedException("You are not authorized to manage stock for this store.");
            }
        }
    }
}