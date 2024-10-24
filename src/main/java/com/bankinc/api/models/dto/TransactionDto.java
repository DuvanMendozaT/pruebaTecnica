package com.bankinc.api.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDto {

    private Long numIdTransaction;
    private Double numAmount;
    private LocalDateTime transactionDate;
    private Long numIdProduct;

}
