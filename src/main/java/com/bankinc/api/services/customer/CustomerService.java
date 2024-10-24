package com.bankinc.api.services.customer;

import com.bankinc.api.models.dto.CustomerDto;
import com.bankinc.api.models.entity.TblCustomer;
import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerDto> findAll();
    CustomerDto save(TblCustomer tblCustomer);
    Optional<CustomerDto> findById(long id);
    String deleteById(long id);

}
