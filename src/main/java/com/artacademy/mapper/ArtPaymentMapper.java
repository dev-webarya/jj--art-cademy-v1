package com.artacademy.mapper;

import com.artacademy.dto.response.ArtPaymentResponseDto;
import com.artacademy.entity.ArtPayment;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ArtPaymentMapper {

    @Mapping(target = "orderNumber", ignore = true) // No longer have order object embedded
    @Mapping(target = "razorpayKeyId", ignore = true)
    ArtPaymentResponseDto toDto(ArtPayment payment);
}
