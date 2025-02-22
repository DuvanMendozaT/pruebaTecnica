package com.bankinc.api.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnulationRequest {
    private Long numIdProduct;
    private Long numIdTransaction;
}
