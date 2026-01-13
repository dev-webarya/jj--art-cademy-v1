package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AttributeValueRequestDto {

    @NotNull(message = "AttributeType ID cannot be null")
    private UUID attributeTypeId;

    @NotBlank(message = "Attribute value cannot be blank")
    private String value;
}