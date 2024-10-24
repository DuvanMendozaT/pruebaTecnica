package com.bankinc.api.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBalanceRequest {
    private long numIdProduct;
    private Double numBalance;
}
