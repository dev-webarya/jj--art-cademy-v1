package com.artacademy.mapper;

import com.artacademy.dto.response.ArtOrderResponseDto;
import com.artacademy.entity.ArtOrder;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtOrderMapper {

    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "statusHistory", source = "statusHistory")
    ArtOrderResponseDto toDto(ArtOrder order);

    @Mapping(target = "subtotal", expression = "java(item.getSubtotal())")
    ArtOrderResponseDto.ArtOrderItemDto toItemDto(ArtOrder.OrderItem item);

    ArtOrderResponseDto.ArtOrderStatusHistoryDto toHistoryDto(ArtOrder.StatusHistory history);

    List<ArtOrderResponseDto.ArtOrderItemDto> toItemDtoList(List<ArtOrder.OrderItem> items);

    List<ArtOrderResponseDto.ArtOrderStatusHistoryDto> toHistoryDtoList(List<ArtOrder.StatusHistory> history);
}
