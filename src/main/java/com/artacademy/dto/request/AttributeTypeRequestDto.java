package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AttributeTypeRequestDto {

    @NotBlank(message = "Attribute name cannot be blank")
    @Size(min = 2, max = 100, message = "Attribute name must be between 2 and 100 characters")
    private String name;
}