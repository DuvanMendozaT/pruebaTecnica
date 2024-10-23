package com.bankinc.api.services.products;

import com.bankinc.api.models.dto.request.ProductBalanceRequest;
import com.bankinc.api.models.dto.request.ProductCreationRequest;
import com.bankinc.api.models.entity.TblProducts;


public interface ProductsService {

    public String generateCardNumber(String productId);
    public TblProducts createProduct(ProductCreationRequest request);
    public void activateProduct(long numIdProduct);
    public void blockProduct (long numIdProduct);

    public String balance (long numIdProduct);

    public TblProducts rechargebalance(ProductBalanceRequest request);
}
