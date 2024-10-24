package com.bankinc.api.models.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ProductDto {

    private long numIdProduct;
    private String strProductNumber;
    private LocalDateTime dtmExpirationDate;
    private Double numbalance;
}
