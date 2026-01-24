package com.artacademy.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MaterialVariantResponseDto {
    private String id;
    private String size;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private BigDecimal stock;
}
