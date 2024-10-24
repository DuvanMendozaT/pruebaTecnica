package com.bankinc.api.services.transaction;

import com.bankinc.api.models.dto.TransactionDto;
import com.bankinc.api.models.request.AnulationRequest;
import com.bankinc.api.models.request.PurchaseRequest;


public interface TransactionService {

   TransactionDto purchase(PurchaseRequest purchaseRequest);
   TransactionDto getTransactionById(long id);
   TransactionDto anulateTransaction(AnulationRequest anulationRequest);
}
