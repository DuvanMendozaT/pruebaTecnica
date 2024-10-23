package com.bankinc.api.services.products;

import com.bankinc.api.commmon.Constant;
import com.bankinc.api.models.dto.request.ProductBalanceRequest;
import com.bankinc.api.models.dto.request.ProductCreationRequest;
import com.bankinc.api.models.entity.TblCustomer;
import com.bankinc.api.models.entity.TblProductType;
import com.bankinc.api.models.entity.TblProducts;
import com.bankinc.api.repository.CustomerRepository;
import com.bankinc.api.repository.ProductRepository;
import com.bankinc.api.repository.ProductTypeRepository;
import com.bankinc.api.services.products.ProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ProductsServiceImpl implements ProductsService {
    private final SecureRandom random = new SecureRandom();
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductTypeRepository productTypeRepository;



    @Override
    public String generateCardNumber(String productId) {
        validateProductIdLength(productId);
        String cardNumber = generateRandomNumber(productId);
        while (productRepository.existsById(Long.parseLong(cardNumber))) {
            cardNumber = generateRandomNumber(productId);
        }

        return cardNumber;

    }

    @Override
    public TblProducts createProduct(ProductCreationRequest request) {
        TblCustomer tblCustomer = customerRepository.findById(request.getNumIdCustomer())
                .orElseThrow(() -> new IllegalArgumentException("cliente inexistente"));
        TblProductType tblProductType = productTypeRepository.findById(Constant.ID_PRODUCT_TYPE_CARD)
                .orElseThrow(() -> new IllegalArgumentException("error en el tipo del producto"));

        TblProducts  tblProducts= TblProducts.builder()
                .strProductNumber(request.getStrProductnumber())
                .numIdProducType(tblProductType)
                .numIdCustomer(tblCustomer)
                .build();
        return  productRepository.save(tblProducts);
    }

    @Override
    public void activateProduct(long numIdProduct) {
        TblProducts tblProducts = productRepository.findById(numIdProduct)
                .orElseThrow(() -> new RuntimeException("Producto no existente"));
        tblProducts.setNumActivation(Constant.PRODUCT_ACTIVATE);
        productRepository.save(tblProducts);
    }

    @Override
    public void blockProduct(long numIdProduct) {
        TblProducts tblProducts = productRepository.findById(numIdProduct)
                .orElseThrow(() -> new RuntimeException("Producto no existente"));
        tblProducts.setNumStatus(Constant.PRODUCT_BLOQUED);
        productRepository.save(tblProducts);
    }

    @Override
    public String balance(long numIdProduct) {
        TblProducts tblProducts = productRepository.findById(numIdProduct)
                .orElseThrow(() -> new RuntimeException("Producto no existente"));
        return String.valueOf(tblProducts.getNumbalance());
    }

    @Override
    public TblProducts rechargebalance(ProductBalanceRequest request) {
        TblProducts tblProducts = productRepository.findById(request.getNumIdProduct())
                .orElseThrow(() -> new RuntimeException("Producto no existente"));
        tblProducts.setNumbalance(tblProducts.getNumbalance() + request.getNumBalance());
        return productRepository.save(tblProducts);
    }


    public void  validateProductIdLength(String productId){
        if (productId == null || productId.length() != 6) {
            throw new IllegalArgumentException("El Id del producto debe ser de 6 digitos");
        }
    }

    public String generateRandomNumber(String productId){
        StringBuilder cardNumber = new StringBuilder(productId);
        for (int i = 0; i < 10; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }
}
