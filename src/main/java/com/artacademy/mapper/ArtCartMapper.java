package com.artacademy.mapper;

import com.artacademy.dto.response.ArtCartResponseDto;
import com.artacademy.entity.ArtCartItem;
import com.artacademy.entity.ArtShoppingCart;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtCartMapper {

    @Mapping(target = "items", source = "items")
    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    ArtCartResponseDto toDto(ArtShoppingCart cart);

    @Mapping(target = "subtotal", expression = "java(item.getSubtotal())")
    ArtCartResponseDto.ArtCartItemDto toItemDto(ArtCartItem item);

    List<ArtCartResponseDto.ArtCartItemDto> toItemDtoList(List<ArtCartItem> items);
}
