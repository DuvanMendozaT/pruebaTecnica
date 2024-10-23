package com.bankinc.api.services.customer;

import com.bankinc.api.models.entity.TblCustomer;
import com.bankinc.api.repository.CustomerRepository;
import com.bankinc.api.services.customer.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceimpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<TblCustomer> finAll() {
        return customerRepository.findAll();
    }

    @Override
    public TblCustomer save(TblCustomer tblCustomer) {
        return customerRepository.save(tblCustomer);
    }

    @Override
    public Optional<TblCustomer> findById(long id) {
        return customerRepository.findById(id);
    }

    @Override
    public String deleteById(long id) {
        try {
            customerRepository.deleteById(id);
            return "cliente eliminado";
        }catch (Exception e){
            return "fallo en la eliminacion";
        }
    }
}
