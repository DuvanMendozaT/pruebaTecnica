package com.bankinc.api.services.products;

import com.bankinc.api.models.dto.ProductDto;
import com.bankinc.api.models.request.ProductBalanceRequest;
import com.bankinc.api.models.request.ProductCreationRequest;


public interface ProductsService {

    String generateCardNumber(String productId);
    ProductDto createProduct(ProductCreationRequest request);
    void activateProduct(long numIdProduct);
    void blockProduct (long numIdProduct);

    String balance (long numIdProduct);

    ProductDto rechargebalance(ProductBalanceRequest request);
}
