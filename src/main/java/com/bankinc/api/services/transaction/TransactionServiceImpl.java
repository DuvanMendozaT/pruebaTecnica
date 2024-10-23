package com.bankinc.api.services.transaction;

import com.bankinc.api.commmon.Constant;
import com.bankinc.api.models.dto.request.AnulationRequest;
import com.bankinc.api.models.dto.request.PurchaseRequest;
import com.bankinc.api.models.entity.TblProducts;
import com.bankinc.api.models.entity.TblTransaction;
import com.bankinc.api.repository.ProductRepository;
import com.bankinc.api.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public TblTransaction purchase(PurchaseRequest purchaseRequest) {
        Long numIdProduct = purchaseRequest.getNumIdProduct();
        TblProducts tblProducts = productRepository.findById(numIdProduct)
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        if (tblProducts.getNumActivation() != Constant.PRODUCT_ACTIVATE) {
            throw new IllegalArgumentException("La tarjeta no ha sido activada");
        }
        if (tblProducts.getNumStatus() == 0) {
            throw new IllegalArgumentException("La tarjeta ha sido bloquedad");
        }
        if (tblProducts.getDtmExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La tarjeta se encuentra expirada");
        }
        if (tblProducts.getNumbalance()< purchaseRequest.getPrice()){
            throw  new IllegalArgumentException("Saldo insuficiente");
        }
        tblProducts.setNumbalance(tblProducts.getNumbalance() - purchaseRequest.getPrice());

        productRepository.save(tblProducts);

        TblTransaction tblTransaction = TblTransaction.builder()
                .numAmount(purchaseRequest.getPrice())
                .numIdProduct(tblProducts)
                .build();

        return transactionRepository.save(tblTransaction);
    }

    @Override
    public TblTransaction getTransactionById(long numIdTransaction) {
            return transactionRepository.findById(numIdTransaction)
                    .orElseThrow(() -> new IllegalArgumentException("Transaccion no existente"));
    }

    @Override
    public TblTransaction anulateTransaction(AnulationRequest anulationRequest) {
        TblTransaction tblTransaction = transactionRepository.findById(anulationRequest.getNumIdTransaction())
                .orElseThrow(() -> new IllegalArgumentException("Transaccion no encontrada"));

        if (tblTransaction.getTransactionDate().isBefore(LocalDateTime.now().minusHours(Constant.ANULATE_TRANSACTION_LIMIT_TIME))) {
            throw new IllegalArgumentException("La transaccion supera el limite de tiempo de 24 horas");
        }

        TblProducts tblProducts = productRepository.findById(anulationRequest.getNumIdTransaction())
                .orElseThrow(() -> new IllegalArgumentException("producto no encontrado."));

        tblProducts.setNumbalance(tblProducts.getNumbalance() + tblTransaction.getNumAmount());

        tblTransaction.setStrStatus("anulada");

        productRepository.save(tblProducts);
        return transactionRepository.save(tblTransaction);
    }
}
