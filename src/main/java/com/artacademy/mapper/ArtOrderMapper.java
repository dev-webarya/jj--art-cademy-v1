package com.artacademy.mapper;

import com.artacademy.dto.response.ArtOrderResponseDto;
import com.artacademy.entity.ArtOrder;
import com.artacademy.entity.ArtOrderItem;
import com.artacademy.entity.ArtOrderStatusHistory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtOrderMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "items", source = "items")
    @Mapping(target = "statusHistory", source = "statusHistory")
    ArtOrderResponseDto toDto(ArtOrder order);

    @Mapping(target = "subtotal", expression = "java(item.getSubtotal())")
    ArtOrderResponseDto.ArtOrderItemDto toItemDto(ArtOrderItem item);

    ArtOrderResponseDto.ArtOrderStatusHistoryDto toHistoryDto(ArtOrderStatusHistory history);

    List<ArtOrderResponseDto.ArtOrderItemDto> toItemDtoList(List<ArtOrderItem> items);

    List<ArtOrderResponseDto.ArtOrderStatusHistoryDto> toHistoryDtoList(List<ArtOrderStatusHistory> history);
}
