package com.bankinc.api.services.transaction;

import com.bankinc.api.commmon.Constant;
import com.bankinc.api.models.dto.request.AnulationRequest;
import com.bankinc.api.models.dto.request.PurchaseRequest;
import com.bankinc.api.models.entity.TblCustomer;
import com.bankinc.api.models.entity.TblProductType;
import com.bankinc.api.models.entity.TblProducts;
import com.bankinc.api.models.entity.TblTransaction;
import com.bankinc.api.repository.ProductRepository;
import com.bankinc.api.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TblProducts genericProduct;
    private TblTransaction genericTransaction;
    private PurchaseRequest samplePurchaseRequest;
    private AnulationRequest sampleAnulationRequest;

    @BeforeEach
    void setUp() {
        TblCustomer customer = new TblCustomer();
        customer.setNumIdCustomer(1L);

        TblProductType productType = new TblProductType();
        productType.setNumIdProductType(1);

        genericProduct = TblProducts.builder()
                .numIdProduct(1L)
                .strProductNumber("1234567890123456")
                .numIdProducType(productType)
                .numIdCustomer(customer)
                .dtmExpirationDate(LocalDateTime.now().plusYears(1))
                .dtmCreationDate(LocalDateTime.now())
                .numbalance(1000.0)
                .numActivation(Constant.PRODUCT_ACTIVATE)
                .numStatus(1)
                .build();

        genericTransaction = TblTransaction.builder()
                .numIdTransaction(1L)
                .numAmount(100.0)
                .transactionDate(LocalDateTime.now())
                .strStatus("exitosa")
                .numIdProduct(genericProduct)
                .build();

        samplePurchaseRequest = new PurchaseRequest();
        samplePurchaseRequest.setNumIdProduct(1L);
        samplePurchaseRequest.setPrice(100.0);

        sampleAnulationRequest = new AnulationRequest();
        sampleAnulationRequest.setNumIdTransaction(1L);
        sampleAnulationRequest.setNumIdTransaction(1L);
    }

    @Nested
    class PurchaseTests {
        @Test

        void shouldSuccessfullyProcessValidPurchase() {
            // Arrange
            when(productRepository.findById(1L)).thenReturn(Optional.of(genericProduct));
            when(productRepository.save(any(TblProducts.class))).thenReturn(genericProduct);
            when(transactionRepository.save(any(TblTransaction.class))).thenReturn(genericTransaction);

            // Act
            TblTransaction result = transactionService.purchase(samplePurchaseRequest);

            // Assert
            assertNotNull(result);
            assertEquals(genericTransaction.getNumAmount(), result.getNumAmount());
            assertEquals(900.0, genericProduct.getNumbalance()); // 1000 - 100
            verify(productRepository).findById(1L);
            verify(productRepository).save(genericProduct);
            verify(transactionRepository).save(any(TblTransaction.class));
        }

        @Test
        void shouldThrowExceptionWhenCardNotActivated() {
            // Arrange
            genericProduct.setNumActivation(0);
            when(productRepository.findById(1L)).thenReturn(Optional.of(genericProduct));

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> transactionService.purchase(samplePurchaseRequest));
            assertEquals("La tarjeta no ha sido activada", exception.getMessage());
            verify(productRepository, never()).save(any());
            verify(transactionRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when card is blocked")
        void shouldThrowExceptionWhenCardBlocked() {
            // Arrange
            genericProduct.setNumStatus(0);
            when(productRepository.findById(1L)).thenReturn(Optional.of(genericProduct));

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> transactionService.purchase(samplePurchaseRequest));
            assertEquals("La tarjeta ha sido bloquedad", exception.getMessage());
            verify(productRepository, never()).save(any());
            verify(transactionRepository, never()).save(any());
        }

        @Test
        void shouldThrowExceptionWhenCardExpired() {
            // Arrange
            genericProduct.setDtmExpirationDate(LocalDateTime.now().minusDays(1));
            when(productRepository.findById(1L)).thenReturn(Optional.of(genericProduct));

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> transactionService.purchase(samplePurchaseRequest));
            assertEquals("La tarjeta se encuentra expirada", exception.getMessage());
            verify(productRepository, never()).save(any());
            verify(transactionRepository, never()).save(any());
        }

        @Test
        void shouldThrowExceptionWhenInsufficientBalance() {
            // Arrange
            samplePurchaseRequest.setPrice(2000.0); // More than available balance
            when(productRepository.findById(1L)).thenReturn(Optional.of(genericProduct));

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> transactionService.purchase(samplePurchaseRequest));
            assertEquals("Saldo insuficiente", exception.getMessage());
            verify(productRepository, never()).save(any());
            verify(transactionRepository, never()).save(any());
        }
    }

    @Nested
    class GetTransactionByIdTests {

        @Test
        void shouldReturnTransactionWhenExists() {
            // Arrange
            when(transactionRepository.findById(1L)).thenReturn(Optional.of(genericTransaction));

            // Act
            TblTransaction result = transactionService.getTransactionById(1L);

            // Assert
            assertNotNull(result);
            assertEquals(genericTransaction.getNumIdTransaction(), result.getNumIdTransaction());
            verify(transactionRepository).findById(1L);
        }

        @Test
        void shouldThrowExceptionWhenTransactionNotFound() {
            // Arrange
            when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> transactionService.getTransactionById(1L));
            assertEquals("Transaccion no existente", exception.getMessage());
            verify(transactionRepository).findById(1L);
        }
    }

    @Nested
    class AnulateTransactionTests {

        @Test
        void shouldSuccessfullyAnnulTransactionWithinTimeLimit() {
            // Arrange
            when(transactionRepository.findById(1L)).thenReturn(Optional.of(genericTransaction));
            when(productRepository.findById(1L)).thenReturn(Optional.of(genericProduct));
            when(productRepository.save(any(TblProducts.class))).thenReturn(genericProduct);
            when(transactionRepository.save(any(TblTransaction.class))).thenReturn(genericTransaction);

            // Act
            TblTransaction result = transactionService.anulateTransaction(sampleAnulationRequest);

            // Assert
            assertNotNull(result);
            assertEquals("anulada", result.getStrStatus());
            assertEquals(1100.0, genericProduct.getNumbalance()); // 1000 + 100
            verify(transactionRepository).findById(1L);
            verify(productRepository).findById(1L);
            verify(productRepository).save(genericProduct);
            verify(transactionRepository).save(genericTransaction);
        }

        @Test
        void shouldThrowExceptionWhenTransactionTooOld() {
            // Arrange
            genericTransaction.setTransactionDate(LocalDateTime.now().minusHours(25));
            when(transactionRepository.findById(1L)).thenReturn(Optional.of(genericTransaction));

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> transactionService.anulateTransaction(sampleAnulationRequest));
            assertEquals("La transaccion supera el limite de tiempo de 24 horas", exception.getMessage());
            verify(transactionRepository).findById(1L);
            verify(productRepository, never()).findById(anyLong());
            verify(productRepository, never()).save(any());
            verify(transactionRepository, never()).save(any());
        }

        @Test
        void shouldThrowExceptionWhenTransactionNotFound() {
            // Arrange
            when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> transactionService.anulateTransaction(sampleAnulationRequest));
            assertEquals("Transaccion no encontrada", exception.getMessage());
            verify(transactionRepository).findById(1L);
            verify(productRepository, never()).findById(anyLong());
        }

        @Test
        void shouldThrowExceptionWhenProductNotFound() {
            // Arrange
            when(transactionRepository.findById(1L)).thenReturn(Optional.of(genericTransaction));
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            // Act & Assert
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> transactionService.anulateTransaction(sampleAnulationRequest));
            assertEquals("producto no encontrado.", exception.getMessage());
            verify(transactionRepository).findById(1L);
            verify(productRepository).findById(1L);
            verify(productRepository, never()).save(any());
            verify(transactionRepository, never()).save(any());
        }
    }
}