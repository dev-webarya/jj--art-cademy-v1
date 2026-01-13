package com.artacademy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class StoreRequestDto {

    @NotBlank(message = "Store name cannot be blank")
    @Size(max = 255)
    private String name;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    private BigDecimal latitude;
    private BigDecimal longitude;
    private String operatingHours;

    @Size(max = 20)
    private String contactPhone;
}