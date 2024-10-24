package com.bankinc.api.services.products;

import com.bankinc.api.commmon.Constant;
import com.bankinc.api.models.dto.ProductDto;
import com.bankinc.api.models.mappers.ProductMapper;
import com.bankinc.api.models.request.ProductBalanceRequest;
import com.bankinc.api.models.request.ProductCreationRequest;
import com.bankinc.api.models.entity.TblCustomer;
import com.bankinc.api.models.entity.TblProductType;
import com.bankinc.api.models.entity.TblProducts;
import com.bankinc.api.repository.CustomerRepository;
import com.bankinc.api.repository.ProductRepository;
import com.bankinc.api.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
public class ProductsServiceImpl implements ProductsService {
    private final SecureRandom random = new SecureRandom();
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductTypeRepository productTypeRepository;
    @Autowired
    ProductMapper productMapper;

    @Override
    public String generateCardNumber(String numIdProduct) {
        validateProductIdLength(numIdProduct);
        String cardNumber = generateRandomNumber(numIdProduct);
        while (productRepository.existsById(Long.parseLong(cardNumber))) {
            cardNumber = generateRandomNumber(numIdProduct);
        }
        return cardNumber;
    }

    @Override
    public ProductDto createProduct(ProductCreationRequest request) {
        TblCustomer tblCustomer = findEntityById(customerRepository.findById(request.getNumIdCustomer()),Constant.NO_EXIST_CLIENT_MESSAGE);
        TblProductType tblProductType = findEntityById(productTypeRepository.findById(Constant.ID_PRODUCT_TYPE_CARD),Constant.NO_EXIST_PRODUCT_TYPE_MESSAGE);
        TblProducts  tblProducts= TblProducts.builder()
                .strProductNumber(request.getStrProductnumber())
                .numIdProducType(tblProductType)
                .numIdCustomer(tblCustomer)
                .build();
        return  productMapper.toDto(productRepository.save(tblProducts));
    }

    @Override
    public void activateProduct(long numIdProduct) {
        TblProducts tblProducts = findEntityById(productRepository.findById(numIdProduct),Constant.NO_EXIST_CLIENT_MESSAGE);
        tblProducts.setNumActivation(Constant.PRODUCT_ACTIVATE);
        productRepository.save(tblProducts);
    }

    @Override
    public void blockProduct(long numIdProduct) {
        TblProducts tblProducts = findEntityById(productRepository.findById(numIdProduct),Constant.NO_EXIST_PRODUCT_MESSAGE);
        tblProducts.setNumStatus(Constant.PRODUCT_BLOQUED);
        productRepository.save(tblProducts);
    }

    @Override
    public String balance(long numIdProduct) {
        TblProducts tblProducts = findEntityById(productRepository.findById(numIdProduct),Constant.NO_EXIST_PRODUCT_MESSAGE);
        return String.valueOf(tblProducts.getNumbalance());
    }

    @Override
    public ProductDto rechargebalance(ProductBalanceRequest request) {
        TblProducts tblProducts = findEntityById(productRepository.findById(request.getNumIdProduct()),Constant.NO_EXIST_PRODUCT_MESSAGE);
        tblProducts.setNumbalance(tblProducts.getNumbalance() + request.getNumBalance());
        return productMapper.toDto(productRepository.save(tblProducts));
    }


    public void  validateProductIdLength(String productId){
        if (productId == null || productId.length() != Constant.PRODUCT_TYPE_NUMBER_LENGTH) {
            throw new IllegalArgumentException(Constant.INVALID_LENGTH_ID_PRODUCT_TYPE);
        }
    }

    public String generateRandomNumber(String productId){
        final StringBuilder cardNumber = new StringBuilder(productId);
        for (int i = 0; i < 10; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }
    private <T> T findEntityById(Optional<T> entity, String errorMessage) {
        return entity.orElseThrow(() -> new IllegalArgumentException(errorMessage));
    }

}
