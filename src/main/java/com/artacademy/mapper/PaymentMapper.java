package com.artacademy.mapper;

import com.artacademy.dto.response.PaymentResponseDto;
import com.artacademy.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    @Mapping(target = "razorpayOrderId", ignore = true) // Set dynamically by service
    @Mapping(target = "keyId", ignore = true) // Set dynamically by service
    PaymentResponseDto toDto(Payment payment);
}