package com.bankinc.api.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBalanceRequest {
    @NotNull
    private long numIdProduct;

    @NotNull
    private Double numBalance;
}
