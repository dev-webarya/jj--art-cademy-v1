package com.artacademy.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MaterialVariantRequestDto {
    private String id;
    private String size;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private BigDecimal stock;
}
