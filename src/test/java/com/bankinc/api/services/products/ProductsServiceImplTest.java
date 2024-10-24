package com.bankinc.api.services.products;

import com.bankinc.api.commmon.Constant;
import com.bankinc.api.models.dto.ProductDto;
import com.bankinc.api.models.entity.TblCustomer;
import com.bankinc.api.models.entity.TblProductType;
import com.bankinc.api.models.entity.TblProducts;
import com.bankinc.api.models.mappers.ProductMapper;
import com.bankinc.api.models.request.ProductBalanceRequest;
import com.bankinc.api.models.request.ProductCreationRequest;
import com.bankinc.api.repository.CustomerRepository;
import com.bankinc.api.repository.ProductRepository;
import com.bankinc.api.repository.ProductTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductsServiceImplTest {
    @InjectMocks
    private ProductsServiceImpl productsService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductTypeRepository productTypeRepository;

    @Mock
    private ProductMapper productMapper;

    private TblCustomer tblCustomer;
    private TblProductType tblProductType;
    private TblProducts tblProducts;
    private ProductDto productDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        tblCustomer = new TblCustomer();
        tblCustomer.setNumIdCustomer(1L);

        tblProductType = new TblProductType();
        tblProductType.setNumIdProductType(1);

        tblProducts = new TblProducts();
        tblProducts.setNumIdProduct(1L);
        tblProducts.setNumbalance(100.0);

        productDto = new ProductDto();
        productDto.setNumIdProduct(1L);
    }

    @Test
    public void testGenerateCardNumber_UniqueCardNumber() {
        String numIdProduct = "1234567890";
        when(productRepository.existsById(anyLong())).thenReturn(false);

        String cardNumber = productsService.generateCardNumber(numIdProduct);

        assertTrue(cardNumber.startsWith(numIdProduct));
        verify(productRepository, atLeastOnce()).existsById(anyLong());
    }

    @Test
    public void testCreateProduct() {
        ProductCreationRequest request = new ProductCreationRequest();
        request.setNumIdCustomer(tblCustomer.getNumIdCustomer());
        request.setStrProductnumber("Product1");

        when(customerRepository.findById(request.getNumIdCustomer())).thenReturn(Optional.of(tblCustomer));
        when(productTypeRepository.findById(Constant.ID_PRODUCT_TYPE_CARD)).thenReturn(Optional.of(tblProductType));
        when(productRepository.save(any(TblProducts.class))).thenReturn(tblProducts);
        when(productMapper.toDto(tblProducts)).thenReturn(productDto);

        ProductDto result = productsService.createProduct(request);

        assertEquals(productDto, result);
        verify(productRepository, times(1)).save(any(TblProducts.class));
    }

    @Test
    public void testActivateProduct() {
        long numIdProduct = 1L;
        when(productRepository.findById(numIdProduct)).thenReturn(Optional.of(tblProducts));

        productsService.activateProduct(numIdProduct);

        assertEquals(Constant.PRODUCT_ACTIVATE, tblProducts.getNumActivation());
        verify(productRepository, times(1)).save(tblProducts);
    }

    @Test
    public void testBlockProduct() {
        long numIdProduct = 1L;
        when(productRepository.findById(numIdProduct)).thenReturn(Optional.of(tblProducts));

        productsService.blockProduct(numIdProduct);

        assertEquals(Constant.PRODUCT_BLOQUED, tblProducts.getNumStatus());
        verify(productRepository, times(1)).save(tblProducts);
    }

    @Test
    public void testBalance() {
        long numIdProduct = 1L;
        when(productRepository.findById(numIdProduct)).thenReturn(Optional.of(tblProducts));

        String balance = productsService.balance(numIdProduct);

        assertEquals("100.0", balance);
    }

    @Test
    public void testRechargeBalance() {
        ProductBalanceRequest request = new ProductBalanceRequest();
        request.setNumIdProduct(tblProducts.getNumIdProduct());
        request.setNumBalance(50.0);

        when(productRepository.findById(request.getNumIdProduct())).thenReturn(Optional.of(tblProducts));
        when(productRepository.save(tblProducts)).thenReturn(tblProducts);
        when(productMapper.toDto(tblProducts)).thenReturn(productDto);

        ProductDto result = productsService.rechargebalance(request);

        assertEquals(productDto, result);
        assertEquals(150.0, tblProducts.getNumbalance());
    }

    @Test
    public void testValidateProductIdLength_Invalid() {
        String invalidProductId = "123";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productsService.validateProductIdLength(invalidProductId);
        });

        assertEquals(Constant.INVALID_LENGTH_ID_PRODUCT_TYPE, exception.getMessage());
    }

    @Test
    public void testValidateProductIdLength_Valid() {
        String validProductId = "1234567890";

        assertDoesNotThrow(() -> {
            productsService.validateProductIdLength(validProductId);
        });
    }


}