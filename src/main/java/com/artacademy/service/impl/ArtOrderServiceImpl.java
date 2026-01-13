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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
    @Transactional
    public ArtOrderResponseDto createOrder(ArtOrderRequestDto request) {
        log.info("Creating order with {} items", request.getItems().size());
        User user = getCurrentUser();

        ArtOrder order = ArtOrder.builder()
                .orderNumber(generateOrderNumber())
                .user(user)
                .status(OrderStatus.PAYMENT_PENDING)
                .shippingAddress(request.getShippingAddress())
                .billingAddress(request.getBillingAddress() != null ? request.getBillingAddress()
                        : request.getShippingAddress())
                .totalPrice(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (ArtOrderRequestDto.ArtOrderItemDto itemDto : request.getItems()) {
            ArtOrderItem orderItem = createOrderItem(order, itemDto.getItemId(), itemDto.getItemType(),
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
    @Transactional
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
                .user(user)
                .status(OrderStatus.PAYMENT_PENDING)
                .shippingAddress(request.getShippingAddress())
                .billingAddress(request.getBillingAddress() != null ? request.getBillingAddress()
                        : request.getShippingAddress())
                .totalPrice(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (ArtCartItem cartItem : cart.getItems()) {
            ArtOrderItem orderItem = ArtOrderItem.builder()
                    .order(order)
                    .itemId(cartItem.getItemId())
                    .itemType(cartItem.getItemType())
                    .itemName(cartItem.getItemName())
                    .imageUrl(cartItem.getImageUrl())
                    .unitPrice(cartItem.getUnitPrice())
                    .quantity(cartItem.getQuantity())
                    .build();
            order.getItems().add(orderItem);
            total = total.add(orderItem.getSubtotal());
        }

        order.setTotalPrice(total);
        order.addStatusHistory(OrderStatus.PAYMENT_PENDING, "Order created from cart checkout");

        // Clear cart after checkout
        cart.getItems().clear();
        cartRepository.save(cart);

        ArtOrder savedOrder = orderRepository.save(order);
        log.info("Cart checkout complete, order number: {}", savedOrder.getOrderNumber());
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public ArtOrderResponseDto getById(UUID id) {
        ArtOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtOrder", "id", id));
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtOrderResponseDto> getAll(OrderStatus status, String orderNumber, BigDecimal minPrice,
            BigDecimal maxPrice, LocalDateTime startDate, LocalDateTime endDate, UUID userId, Pageable pageable) {
        // For simplicity, just return all orders. In production, use Specification
        // pattern.
        return orderRepository.findAll(pageable).map(orderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ArtOrderResponseDto> getMyOrders(Pageable pageable) {
        User user = getCurrentUser();
        return orderRepository.findByUserId(user.getId(), pageable).map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public ArtOrderResponseDto updateStatus(UUID id, OrderStatus status, String notes) {
        log.info("Updating order {} status to: {}", id, status);
        ArtOrder order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ArtOrder", "id", id));

        order.setStatus(status);
        order.addStatusHistory(status, notes);

        // Handle stock deduction on PROCESSING status
        if (status == OrderStatus.PROCESSING) {
            deductStock(order);
        }

        return orderMapper.toDto(orderRepository.save(order));
    }

    private ArtOrderItem createOrderItem(ArtOrder order, UUID itemId, ArtItemType itemType, Integer quantity) {
        String itemName;
        String imageUrl;
        BigDecimal unitPrice;

        if (itemType == ArtItemType.ARTWORK) {
            ArtWorks artwork = artWorksRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", itemId));
            itemName = artwork.getName();
            imageUrl = artwork.getImageUrl();
            unitPrice = artwork.getBasePrice();
        } else {
            ArtMaterials material = artMaterialsRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", itemId));
            itemName = material.getName();
            imageUrl = material.getImageUrl();
            BigDecimal discount = BigDecimal.valueOf(material.getDiscount()).divide(BigDecimal.valueOf(100));
            unitPrice = material.getBasePrice().subtract(material.getBasePrice().multiply(discount));
        }

        return ArtOrderItem.builder()
                .order(order)
                .itemId(itemId)
                .itemType(itemType)
                .itemName(itemName)
                .imageUrl(imageUrl)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .build();
    }

    private void deductStock(ArtOrder order) {
        for (ArtOrderItem item : order.getItems()) {
            if (item.getItemType() == ArtItemType.MATERIAL) {
                ArtMaterials material = artMaterialsRepository.findById(item.getItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("ArtMaterials", "id", item.getItemId()));
                BigDecimal newStock = material.getStock().subtract(BigDecimal.valueOf(item.getQuantity()));
                material.setStock(newStock.max(BigDecimal.ZERO));
                artMaterialsRepository.save(material);
            }
            // ArtWorks are unique, typically marked as sold/inactive after purchase
            if (item.getItemType() == ArtItemType.ARTWORK) {
                ArtWorks artwork = artWorksRepository.findById(item.getItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("ArtWorks", "id", item.getItemId()));
                artwork.setActive(false); // Mark as sold
                artWorksRepository.save(artwork);
            }
        }
    }

    private String generateOrderNumber() {
        return "ART-" + System.currentTimeMillis();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
