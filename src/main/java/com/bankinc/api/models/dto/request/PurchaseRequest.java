package com.bankinc.api.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseRequest {
    @NotNull
    private long numIdProduct;

    @NonNull
    private Double price;
}
