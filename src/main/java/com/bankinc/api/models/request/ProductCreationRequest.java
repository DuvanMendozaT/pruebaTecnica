package com.bankinc.api.models.request;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class ProductCreationRequest {
    private String strProductnumber;

    private Long numIdCustomer;
}