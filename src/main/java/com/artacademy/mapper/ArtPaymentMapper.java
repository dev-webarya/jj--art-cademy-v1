package com.artacademy.mapper;

import com.artacademy.dto.response.ArtPaymentResponseDto;
import com.artacademy.entity.ArtPayment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtPaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "orderNumber", source = "order.orderNumber")
    @Mapping(target = "razorpayKeyId", ignore = true)
    ArtPaymentResponseDto toDto(ArtPayment payment);
}
