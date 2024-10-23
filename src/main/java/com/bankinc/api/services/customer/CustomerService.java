package com.bankinc.api.services.customer;

import com.bankinc.api.models.entity.TblCustomer;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    public List<TblCustomer> finAll();
    public TblCustomer save(TblCustomer tblCustomer);
    public Optional<TblCustomer> findById(long id);
    public String deleteById(long id);

}
