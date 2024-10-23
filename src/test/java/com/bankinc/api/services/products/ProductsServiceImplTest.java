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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductsServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductTypeRepository productTypeRepository;

    @InjectMocks
    private ProductsServiceImpl productsService;

    private TblCustomer genericCustomer;
    private TblProductType genericProductType;
    private TblProducts genericProduct;
    private ProductCreationRequest sampleCreationRequest;

    @BeforeEach
    void setUp() {
        genericCustomer = new TblCustomer();
        genericCustomer.setNumIdCustomer(1L);
        genericCustomer.setStrFirstName("pepito");
        genericCustomer.setStrlastName("perez");

        genericProductType = new TblProductType();
        genericProductType.setNumIdProductType(Constant.ID_PRODUCT_TYPE_CARD);
        genericProductType.setStrProductNumber("123456");
        genericProductType.setStrDescription("tarjeta de credito");

        genericProduct = TblProducts.builder()
                .numIdProduct(1L)
                .strProductNumber("1234567890123456")
                .numIdProducType(genericProductType)
                .numIdCustomer(genericCustomer)
                .numbalance(0.0)
                .numActivation(0)
                .numStatus(1)
                .build();

        sampleCreationRequest = new ProductCreationRequest();
        sampleCreationRequest.setNumIdCustomer(1L);
        sampleCreationRequest.setStrProductnumber("1234567890123456");
    }

    @Nested
    class GenerateCardNumberTests {

        @Test
        void shouldGenerateValidCardNumber() {
            // Arrange
            String productId = "123456";
            when(productRepository.existsById(anyLong())).thenReturn(false);

            // Act
            String cardNumber = productsService.generateCardNumber(productId);

            // Assert
            assertNotNull(cardNumber);
            assertEquals(16, cardNumber.length());
            assertTrue(cardNumber.startsWith(productId));
            verify(productRepository).existsById(anyLong());
        }

        @Test
        void shouldThrowExceptionWhenProductIdLengthInvalid() {
            // Arrange
            String invalidProductId = "12345";

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> productsService.generateCardNumber(invalidProductId),
                    "El Id del producto debe ser de 6 digitos");
            verify(productRepository, never()).existsById(anyLong());
        }

        @Test
        void shouldRegenerateNumberWhenFirstNumberExists() {
            // Arrange
            String productId = "123456";
            when(productRepository.existsById(anyLong()))
                    .thenReturn(true)
                    .thenReturn(false);

            // Act
            String cardNumber = productsService.generateCardNumber(productId);

            // Assert
            assertNotNull(cardNumber);
            assertEquals(16, cardNumber.length());
            assertTrue(cardNumber.startsWith(productId));
            verify(productRepository, times(2)).existsById(anyLong());
        }
    }

    @Nested
    class CreateProductTests {

        @Test
        void shouldSuccessfullyCreateProduct() {
            // Arrange
            when(customerRepository.findById(1L)).thenReturn(Optional.of(genericCustomer));
            when(productTypeRepository.findById(Constant.ID_PRODUCT_TYPE_CARD))
                    .thenReturn(Optional.of(genericProductType));
            when(productRepository.save(any(TblProducts.class))).thenReturn(genericProduct);

            // Act
            TblProducts result = productsService.createProduct(sampleCreationRequest);

            // Assert
            assertNotNull(result);
            assertEquals(genericProduct.getStrProductNumber(), result.getStrProductNumber());
            verify(customerRepository).findById(1L);
            verify(productTypeRepository).findById(Constant.ID_PRODUCT_TYPE_CARD);
            verify(productRepository).save(any(TblProducts.class));
        }

        @Test
        void shouldThrowExceptionWhenCustomerNotFound() {
            // Arrange
            when(customerRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> productsService.createProduct(sampleCreationRequest),
                    "cliente inexistente");
            verify(productRepository, never()).save(any());
        }

        @Test
        void shouldThrowExceptionWhenProductTypeNotFound() {
            // Arrange
            when(customerRepository.findById(1L)).thenReturn(Optional.of(genericCustomer));
            when(productTypeRepository.findById(Constant.ID_PRODUCT_TYPE_CARD))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                    () -> productsService.createProduct(sampleCreationRequest),
                    "error en el tipo del producto");
            verify(productRepository, never()).save(any());
        }
    }

    @Nested
    class ActivateProductTests {

        @Test
        void shouldSuccessfullyActivateProduct() {
            // Arrange
            when(productRepository.findById(1L)).thenReturn(Optional.of(genericProduct));
            when(productRepository.save(any(TblProducts.class))).thenReturn(genericProduct);

            // Act
            productsService.activateProduct(1L);

            // Assert
            assertEquals(Constant.PRODUCT_ACTIVATE, genericProduct.getNumActivation());
            verify(productRepository).findById(1L);
            verify(productRepository).save(genericProduct);
        }

        @Test
        void shouldThrowExceptionWhenProductNotFound() {
            // Arrange
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RuntimeException.class,
                    () -> productsService.activateProduct(1L),
                    "Producto no existente");
            verify(productRepository, never()).save(any());
        }
    }

    @Nested
    class BlockProductTests {

        @Test
        void shouldSuccessfullyBlockProduct() {
            // Arrange
            when(productRepository.findById(1L)).thenReturn(Optional.of(genericProduct));
            when(productRepository.save(any(TblProducts.class))).thenReturn(genericProduct);

            // Act
            productsService.blockProduct(1L);

            // Assert
            assertEquals(Constant.PRODUCT_BLOQUED, genericProduct.getNumStatus());
            verify(productRepository).findById(1L);
            verify(productRepository).save(genericProduct);
        }

        @Test
        void shouldThrowExceptionWhenProductNotFound() {
            // Arrange
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RuntimeException.class,
                    () -> productsService.blockProduct(1L),
                    "Producto no existente");
            verify(productRepository, never()).save(any());
        }
    }

    @Nested
    class BalanceTests {

        @Test
        void shouldReturnCorrectBalance() {
            // Arrange
            genericProduct.setNumbalance(100.0);
            when(productRepository.findById(1L)).thenReturn(Optional.of(genericProduct));

            // Act
            String balance = productsService.balance(1L);

            // Assert
            assertEquals("100.0", balance);
            verify(productRepository).findById(1L);
        }

        @Test
        void shouldThrowExceptionWhenProductNotFound() {
            // Arrange
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RuntimeException.class,
                    () -> productsService.balance(1L),
                    "Producto no existente");
        }
    }

    @Nested
    class RechargeBalanceTests {

        @Test
        void shouldSuccessfullyRechargeBalance() {
            // Arrange
            genericProduct.setNumbalance(100.0);
            ProductBalanceRequest request = new ProductBalanceRequest();
            request.setNumIdProduct(1L);
            request.setNumBalance(50.0);

            when(productRepository.findById(1L)).thenReturn(Optional.of(genericProduct));
            when(productRepository.save(any(TblProducts.class))).thenReturn(genericProduct);

            // Act
            TblProducts result = productsService.rechargebalance(request);

            // Assert
            assertEquals(150.0, result.getNumbalance());
            verify(productRepository).findById(1L);
            verify(productRepository).save(genericProduct);
        }

        @Test
        void shouldThrowExceptionWhenProductNotFound() {
            // Arrange
            ProductBalanceRequest request = new ProductBalanceRequest();
            request.setNumIdProduct(1L);
            request.setNumBalance(50.0);

            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(RuntimeException.class,
                    () -> productsService.rechargebalance(request),
                    "Producto no existente");
            verify(productRepository, never()).save(any());
        }
    }
}