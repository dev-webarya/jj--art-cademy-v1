package com.artacademy.mapper;

import com.artacademy.dto.response.ShipmentResponseDto;
import com.artacademy.entity.Shipment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    @Mapping(target = "orderId", source = "order.id")
    ShipmentResponseDto toDto(Shipment shipment);
}