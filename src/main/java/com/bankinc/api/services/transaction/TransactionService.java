package com.bankinc.api.services.transaction;

import com.bankinc.api.models.dto.request.AnulationRequest;
import com.bankinc.api.models.dto.request.PurchaseRequest;
import com.bankinc.api.models.entity.TblTransaction;


public interface TransactionService {

   public TblTransaction purchase(PurchaseRequest purchaseRequest);
   public TblTransaction getTransactionById(long id);
   public TblTransaction anulateTransaction(AnulationRequest anulationRequest);
}
