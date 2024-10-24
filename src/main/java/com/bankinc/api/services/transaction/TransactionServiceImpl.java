package com.bankinc.api.services.transaction;

import com.bankinc.api.commmon.Constant;
import com.bankinc.api.models.dto.TransactionDto;
import com.bankinc.api.models.mappers.TransactionMapper;
import com.bankinc.api.models.request.AnulationRequest;
import com.bankinc.api.models.request.PurchaseRequest;
import com.bankinc.api.models.entity.TblProducts;
import com.bankinc.api.models.entity.TblTransaction;
import com.bankinc.api.repository.ProductRepository;
import com.bankinc.api.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionMapper transactionMapper;

    @Override
    public TransactionDto purchase(PurchaseRequest purchaseRequest) {
        TblProducts tblProducts = findEntityById(productRepository.findById(purchaseRequest.getNumIdProduct()),Constant.NO_EXIST_PRODUCT_MESSAGE);

        if (tblProducts.getNumActivation() != Constant.PRODUCT_ACTIVATE) {
            throw new IllegalArgumentException(Constant.NO_ACTIVE_PRODUCT_MESSAGE);
        }
        if (tblProducts.getNumStatus() == Constant.PRODUCT_BLOQUED) {
            throw new IllegalArgumentException(Constant.BLOQUED_PRODUCT_MESSAGE);
        }
        if (tblProducts.getDtmExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(Constant.EXPIRED_PRODUCT_MESSAGE);
        }
        if (tblProducts.getNumbalance()< purchaseRequest.getPrice()){
            throw  new IllegalArgumentException(Constant.INSUFFICIENT_BALANCE);
        }
        tblProducts.setNumbalance(tblProducts.getNumbalance() - purchaseRequest.getPrice());

        productRepository.save(tblProducts);

        TblTransaction tblTransaction = TblTransaction.builder()
                .numAmount(purchaseRequest.getPrice())
                .numIdProduct(tblProducts)
                .build();
        return transactionMapper.toDto(transactionRepository.save(tblTransaction));
    }

    @Override
    public TransactionDto getTransactionById(long numIdTransaction) {
        final TblTransaction tblTransaction = findEntityById(transactionRepository.findById(numIdTransaction),Constant.NO_EXIST_TRANSACTION_MESSAGE);
        return transactionMapper.toDto(tblTransaction);
    }

    @Override
    public TransactionDto anulateTransaction(AnulationRequest anulationRequest) {
        final TblTransaction tblTransaction = findEntityById(transactionRepository.findById(anulationRequest.getNumIdTransaction()),Constant.NO_EXIST_TRANSACTION_MESSAGE);

        if (tblTransaction.getTransactionDate().isBefore(LocalDateTime.now().minusHours(Constant.ANULATE_TRANSACTION_LIMIT_TIME))) {
            throw new IllegalArgumentException(Constant.TRANSACTION_TIME_LIMIT_EXCEEDED_MESSAGE);
        }
        final TblProducts tblProducts = findEntityById(productRepository.findById(anulationRequest.getNumIdTransaction()),Constant.NO_EXIST_PRODUCT_MESSAGE);

        tblProducts.setNumbalance(tblProducts.getNumbalance() + tblTransaction.getNumAmount());

        tblTransaction.setStrStatus(Constant.ANULATE_STATUS);

        productRepository.save(tblProducts);
        return transactionMapper.toDto(transactionRepository.save(tblTransaction));
    }

    private <T> T findEntityById(Optional<T> entity, String errorMessage) {
        return entity.orElseThrow(() -> new IllegalArgumentException(errorMessage));
    }
}
