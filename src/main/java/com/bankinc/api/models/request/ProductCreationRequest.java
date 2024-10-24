package com.bankinc.api.models.request;

import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class ProductCreationRequest {
    private String strProductnumber;

    private Long numIdCustomer;

    public String getStrProductnumber() {
        return strProductnumber;
    }

    public Long getNumIdCustomer() {
        return numIdCustomer;
    }

    public void setStrProductnumber(String strProductnumber) {
        this.strProductnumber = strProductnumber;
    }

    public void setNumIdCustomer(Long numIdCustomer) {
        this.numIdCustomer = numIdCustomer;
    }
}