package com.artacademy.mapper;

import com.artacademy.dto.response.CartResponseDto;
import com.artacademy.dto.response.ReviewResponseDto;
import com.artacademy.entity.CartItem;
import com.artacademy.entity.Review;
import com.artacademy.entity.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface EngagementMapper {

    // --- Cart Maps ---
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    // FIX: Sum the quantity of items (e.g., 2 rings + 1 chain = 3 items) instead of
    // counting rows (2 rows)
    @Mapping(target = "totalItems", expression = "java(cart.getItems().stream().mapToInt(com.artacademy.entity.CartItem::getQuantity).sum())")
    CartResponseDto toCartDto(ShoppingCart cart);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "unitPrice", source = "product.basePrice")
    // Note: Assuming you have a way to get primary image, or just null for now
    @Mapping(target = "productImageUrl", ignore = true)
    @Mapping(target = "subTotal", source = ".", qualifiedByName = "calcSubTotal")
    CartResponseDto.CartItemDto toCartItemDto(CartItem item);

    @Named("calcSubTotal")
    default BigDecimal calcSubTotal(CartItem item) {
        if (item.getProduct() == null)
            return BigDecimal.ZERO;
        return item.getProduct().getBasePrice().multiply(BigDecimal.valueOf(item.getQuantity()));
    }

    // --- Review Maps ---
    @Mapping(target = "userName", expression = "java(review.getUser().getFirstName() + \" \" + review.getUser().getLastName().charAt(0) + \".\")")
    ReviewResponseDto toReviewDto(Review review);
}