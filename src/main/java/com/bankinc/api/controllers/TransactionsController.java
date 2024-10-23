package com.bankinc.api.controllers;

import com.bankinc.api.models.dto.request.AnulationRequest;
import com.bankinc.api.models.dto.request.PurchaseRequest;
import com.bankinc.api.models.entity.TblTransaction;
import com.bankinc.api.services.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
@Tag(name ="Transactions resources" )
public class TransactionsController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/purchase")
    @Operation(summary = "Realizar compra")
    public ResponseEntity<TblTransaction> purchase(@RequestBody PurchaseRequest purchaseRequest){
        return ResponseEntity.ok(transactionService.purchase(purchaseRequest));
    }
    @GetMapping("/{transactionId}")
    @Operation(summary = "consultar compra")
    public ResponseEntity<TblTransaction> getTransactionById(@PathVariable Long transactionId) {
        TblTransaction transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }
    @PostMapping("/anulation")
    @Operation(summary = "anular compra")
    public ResponseEntity<TblTransaction> anulateTransaction(@RequestBody AnulationRequest anulationRequest) {
        return ResponseEntity.ok(transactionService.anulateTransaction(anulationRequest));
    }
}


