package com.artacademy.service.impl;

import com.artacademy.dto.request.ArtCartCheckoutRequestDto;
import com.artacademy.dto.request.ArtOrderRequestDto;
import com.artacademy.dto.response.ArtOrderResponseDto;
import com.artacademy.entity.*;
import com.artacademy.enums.ArtItemType;
import com.artacademy.enums.OrderStatus;
import com.artacademy.exception.ResourceNotFoundException;
import com.artacademy.mapper.ArtOrderMapper;
import com.artacademy.repository.*;
import com.artacademy.service.ArtOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Order service implementation for MongoDB.
 * Implements compensating transactions for rollback on payment failure.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ArtOrderServiceImpl implements ArtOrderService {

    private final ArtOrderRepository orderRepository;
    private final ArtCartRepository cartRepository;
    private final ArtWorksRepository artWorksRepository;
    private final ArtMaterialsRepository artMaterialsRepository;
    private final UserRepository userRepository;
    private final ArtOrderMapper orderMapper;

    @Override
    public ArtOrderResponseDto createOrder(ArtOrderRequestDto request) {
        log.info("Creating order with {} items", request.getItems().size());
        User user = getCurrentUser();

        ArtOrder order = ArtOrder.builder()
                .orderNumber(generateOrderNumber())
                .userId(user.getId())
                .status(OrderStatus.PAYMENT_PENDING)
                .shippingAddress(request.getShippingAddress())
                .billingAddress(request.getBillingAddress() != null ? request.getBillingAddress()
                        : request.getShippingAddress())
                .totalPrice(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (ArtOrderRequestDto.ArtOrderItemDto itemDto : request.getItems()) {
            ArtOrderItem orderItem = createOrderItem(itemDto.getItemId(), itemDto.getItemType(),
                    itemDto.getQuantity());
            order.getItems().add(orderItem);
            total = total.add(orderItem.getSubtotal());
        }

        order.setTotalPrice(total);
        order.addStatusHistory(OrderStatus.PAYMENT_PENDING, "Order created");

        ArtOrder savedOrder = orderRepository.save(order);
        log.info("Order created with number: {}", savedOrder.getOrderNumber());
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public ArtOrderResponseDto checkoutCart(ArtCartCheckoutRequestDto request) {
        log.info("Checking out cart for customer");
        User user = getCurrentUser();
        ArtShoppingCart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("ArtShoppingCart", "userId", user.getId()));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        ArtOrder order = ArtOrder.builder()
                .orderNumber(generateOrderNumber())
                .userId(user.getId())
                .status(OrderStatus.PAYMENT_PENDING)
                .shippingAddress(request.getShippingAddress())
                .billingAddress(request.getBillingAddress() != null ? request.getBillingAddress()
                        : request.getShippingAddress())
                .totalPrice(BigDecimal.ZERO)
                .build();

        // Use the order's helper method to add items from cart
        order.addItemsFromCart(cart.getItems());
        order.calculateTotalPrice();
        order.addStatusHistory(OrderStatus.PAYMENT_PENDING, "Order created from cart checkout");

        // Clear cart after checkout
        cart.clearItems();
        cartRepository.save(cart);

        ArtOrder savedOrder = orderRepository.save(order);
        log.info("Cart checkout complete, order number: {}", savedOrder.getOrderNumber());
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public ArtOrderResponseDto getById(String id) {
        ArtOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtOrder", "id", id));
        return orderMapper.toDto(order);
    }

    @Override
    public Page<ArtOrderResponseDto> getAll(OrderStatus status, String orderNumber, BigDecimal minPrice,
            BigDecimal maxPrice, LocalDateTime startDate, LocalDateTime endDate, String userId, Pageable pageable) {
        // For simplicity, filter by status if provided, otherwise return all
        if (status != null) {
            return orderRepository.findByStatus(status, pageable).map(orderMapper::toDto);
        }
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Override
    public Page<ArtOrderResponseDto> getMyOrders(Pageable pageable) {
        User user = getCurrentUser();
        return orderRepository.findByUserId(user.getId(), pageable).map(orderMapper::toDto);
    }

    @Override
    public ArtOrderResponseDto updateStatus(String id, OrderStatus status, String notes) {
        log.info("Updating order {} status to: {}", id, status);
        ArtOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtOrder", "id", id));

        order.setStatus(status);
        order.addStatusHistory(status, notes);

        // Handle stock deduction on PROCESSING status (after successful payment)
        if (status == OrderStatus.PROCESSING) {
            deductStock(order);
        }

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public void rollbackOrder(String orderId, String reason) {
        log.warn("Rolling back order {} due to: {}", orderId, reason);
        ArtOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("ArtOrder", "id", orderId));

        // Only rollback if stock was already deducted (status is PROCESSING or later)
        if (order.getStatus() == OrderStatus.PROCESSING ||
                order.getStatus() == OrderStatus.SHIPPED ||
                order.getStatus() == OrderStatus.DELIVERED) {
            restoreStock(order);
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.addStatusHistory(OrderStatus.CANCELLED, "Order cancelled: " + reason);
        orderRepository.save(order);
        log.info("Order {} rolled back successfully", orderId);
    }

    private ArtOrderItem createOrderItem(String itemId, ArtItemType itemType, Integer quantity) {
        String itemName;
        String imageUrl;
        BigDecimal unitPrice;

        if (itemType == ArtItemType.ARTWORK) {
            ArtWorks artwork = artWorksRepository.findByIdAndDeletedFalse(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", itemId));
            itemName = artwork.getName();
            imageUrl = artwork.getImageUrl();
            unitPrice = artwork.getBasePrice();
        } else {
            ArtMaterials material = artMaterialsRepository.findByIdAndDeletedFalse(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", itemId));
            itemName = material.getName();
            imageUrl = material.getImageUrl();
            if (material.getDiscount() != null && material.getDiscount() > 0) {
                BigDecimal discount = BigDecimal.valueOf(material.getDiscount())
                        .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                unitPrice = material.getBasePrice().subtract(material.getBasePrice().multiply(discount));
            } else {
                unitPrice = material.getBasePrice();
            }
        }

        return ArtOrderItem.builder()
                .id(UUID.randomUUID().toString())
                .itemId(itemId)
                .itemType(itemType)
                .itemName(itemName)
                .imageUrl(imageUrl)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .build();
    }

    private void deductStock(ArtOrder order) {
        log.info("Deducting stock for order: {}", order.getOrderNumber());
        for (ArtOrderItem item : order.getItems()) {
            if (item.getItemType() == ArtItemType.MATERIAL) {
                ArtMaterials material = artMaterialsRepository.findByIdAndDeletedFalse(item.getItemId())
                        .orElse(null);
                if (material != null) {
                    BigDecimal newStock = material.getStock().subtract(BigDecimal.valueOf(item.getQuantity()));
                    material.setStock(newStock.max(BigDecimal.ZERO));
                    artMaterialsRepository.save(material);
                }
            }
            // ArtWorks are unique, mark as sold
            if (item.getItemType() == ArtItemType.ARTWORK) {
                ArtWorks artwork = artWorksRepository.findByIdAndDeletedFalse(item.getItemId())
                        .orElse(null);
                if (artwork != null) {
                    artwork.setActive(false); // Mark as sold
                    artWorksRepository.save(artwork);
                }
            }
        }
    }

    private void restoreStock(ArtOrder order) {
        log.info("Restoring stock for order: {}", order.getOrderNumber());
        for (ArtOrderItem item : order.getItems()) {
            if (item.getItemType() == ArtItemType.MATERIAL) {
                ArtMaterials material = artMaterialsRepository.findById(item.getItemId())
                        .orElse(null);
                if (material != null && !material.isDeleted()) {
                    BigDecimal restoredStock = material.getStock().add(BigDecimal.valueOf(item.getQuantity()));
                    material.setStock(restoredStock);
                    artMaterialsRepository.save(material);
                }
            }
            // Restore artwork availability
            if (item.getItemType() == ArtItemType.ARTWORK) {
                ArtWorks artwork = artWorksRepository.findById(item.getItemId())
                        .orElse(null);
                if (artwork != null && !artwork.isDeleted()) {
                    artwork.setActive(true); // Mark as available again
                    artWorksRepository.save(artwork);
                }
            }
        }
    }

    private String generateOrderNumber() {
        return "ART-" + System.currentTimeMillis();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
