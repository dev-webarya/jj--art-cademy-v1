package com.artacademy.mapper;

import com.artacademy.dto.response.ArtPaymentResponseDto;
import com.artacademy.entity.ArtPayment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtPaymentMapper {

    @Mapping(target = "orderId", source = "order.orderId")
    @Mapping(target = "orderNumber", source = "order.orderNumber")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "razorpayKeyId", ignore = true)
    ArtPaymentResponseDto toDto(ArtPayment payment);
}
