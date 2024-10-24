package com.bankinc.api.services.transaction;

import com.bankinc.api.commmon.Constant;
import com.bankinc.api.models.dto.TransactionDto;
import com.bankinc.api.models.entity.TblProducts;
import com.bankinc.api.models.entity.TblTransaction;
import com.bankinc.api.models.mappers.TransactionMapper;
import com.bankinc.api.models.request.AnulationRequest;
import com.bankinc.api.models.request.PurchaseRequest;
import com.bankinc.api.repository.ProductRepository;
import com.bankinc.api.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class TransactionServiceImplTest {
    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    private TblProducts tblProducts;
    private TblTransaction tblTransaction;
    private TransactionDto transactionDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Inicialización de productos de prueba
        tblProducts = new TblProducts();
        tblProducts.setNumActivation(Constant.PRODUCT_ACTIVATE);
        tblProducts.setNumStatus(Constant.PRODUCT_ACTIVATE);
        tblProducts.setNumbalance(100.0);
        tblProducts.setDtmExpirationDate(LocalDateTime.now().plusDays(1));

        tblTransaction = TblTransaction.builder()
                .numAmount(50.0)
                .numIdProduct(tblProducts)
                .build();

        transactionDto = new TransactionDto();
        transactionDto.setNumAmount(50.0);
    }

    @Test
    public void testPurchase_Success() {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setNumIdProduct(1L);
        purchaseRequest.setPrice(50.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(tblProducts));
        when(transactionMapper.toDto(any())).thenReturn(transactionDto);
        when(transactionRepository.save(any())).thenReturn(tblTransaction);

        TransactionDto result = transactionService.purchase(purchaseRequest);

        assertNotNull(result);
        assertEquals(50.0, result.getNumAmount());
        assertEquals(50.0, tblProducts.getNumbalance());
        verify(productRepository, times(1)).save(tblProducts);
        verify(transactionRepository, times(1)).save(tblTransaction);
    }

    @Test
    public void testPurchase_ProductNotActive() {
        tblProducts.setNumActivation(Constant.PRODUCT_INACTIVATE);
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setNumIdProduct(1L);
        purchaseRequest.setPrice(50.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(tblProducts));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.purchase(purchaseRequest);
        });

        assertEquals(Constant.NO_ACTIVE_PRODUCT_MESSAGE, exception.getMessage());
    }

    @Test
    public void testPurchase_ProductBlocked() {
        tblProducts.setNumStatus(Constant.PRODUCT_BLOQUED);
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setNumIdProduct(1L);
        purchaseRequest.setPrice(50.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(tblProducts));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.purchase(purchaseRequest);
        });

        assertEquals(Constant.BLOQUED_PRODUCT_MESSAGE, exception.getMessage());
    }

    @Test
    public void testPurchase_ProductExpired() {
        tblProducts.setDtmExpirationDate(LocalDateTime.now().minusDays(1));
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setNumIdProduct(1L);
        purchaseRequest.setPrice(50.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(tblProducts));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.purchase(purchaseRequest);
        });

        assertEquals(Constant.EXPIRED_PRODUCT_MESSAGE, exception.getMessage());
    }

    @Test
    public void testPurchase_InsufficientBalance() {
        tblProducts.setNumbalance(30.0);
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setNumIdProduct(1L);
        purchaseRequest.setPrice(50.0);

        when(productRepository.findById(1L)).thenReturn(Optional.of(tblProducts));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.purchase(purchaseRequest);
        });

        assertEquals(Constant.INSUFFICIENT_BALANCE, exception.getMessage());
    }

    @Test
    public void testGetTransactionById_Success() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(tblTransaction));
        when(transactionMapper.toDto(tblTransaction)).thenReturn(transactionDto);

        TransactionDto result = transactionService.getTransactionById(1L);

        assertNotNull(result);
        assertEquals(transactionDto, result);
        verify(transactionRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getTransactionById(1L);
        });

        assertEquals(Constant.NO_EXIST_TRANSACTION_MESSAGE, exception.getMessage());
    }

    @Test
    public void testAnulateTransaction_Success() {
        AnulationRequest anulationRequest = new AnulationRequest();
        anulationRequest.setNumIdTransaction(1L);

        tblTransaction.setTransactionDate(LocalDateTime.now().minusMinutes(30));
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(tblTransaction));
        when(productRepository.findById(1L)).thenReturn(Optional.of(tblProducts));
        when(transactionMapper.toDto(tblTransaction)).thenReturn(transactionDto);

        TransactionDto result = transactionService.anulateTransaction(anulationRequest);

        assertNotNull(result);
        assertEquals(transactionDto, result);
        assertEquals(Constant.ANULATE_STATUS, tblTransaction.getStrStatus());
        assertEquals(80.0, tblProducts.getNumbalance());
        verify(productRepository, times(1)).save(tblProducts);
        verify(transactionRepository, times(1)).save(tblTransaction);
    }

    @Test
    public void testAnulateTransaction_TransactionTimeLimitExceeded() {
        AnulationRequest anulationRequest = new AnulationRequest();
        anulationRequest.setNumIdTransaction(1L);

        tblTransaction.setTransactionDate(LocalDateTime.now().minusHours(1));
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(tblTransaction));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.anulateTransaction(anulationRequest);
        });

        assertEquals(Constant.TRANSACTION_TIME_LIMIT_EXCEEDED_MESSAGE, exception.getMessage());
    }

    @Test
    public void testAnulateTransaction_NotFound() {
        AnulationRequest anulationRequest = new AnulationRequest();
        anulationRequest.setNumIdTransaction(1L);

        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transactionService.anulateTransaction(anulationRequest);
        });

        assertEquals(Constant.NO_EXIST_TRANSACTION_MESSAGE, exception.getMessage());
    }

}