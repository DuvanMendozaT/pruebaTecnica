package com.bankinc.api.services.customer;

import com.bankinc.api.models.dto.CustomerDto;
import com.bankinc.api.models.entity.TblCustomer;
import com.bankinc.api.models.mappers.CustomerMapper;
import com.bankinc.api.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceimpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<CustomerDto> findAll() {
        return customerMapper.toDtos(customerRepository.findAll());
    }

    @Override
    public CustomerDto save(TblCustomer tblCustomer) {
        return customerMapper.toDto(customerRepository.save(tblCustomer));
    }

    @Override
    public Optional<CustomerDto> findById(long id) {
            return customerRepository.findById(id)
                .map(tblcustomer -> customerMapper.toDto(tblcustomer));
    }

    @Override
    public String deleteById(long id) {
        if(customerRepository.existsById(id)){
            try {
                customerRepository.deleteById(id);
                return "Cliente eliminado con éxito";
            } catch (DataAccessException e) {
                return "Fallo en la eliminación: " + e.getMessage();
            }
        } else {
            return "Cliente no encontrado";
        }
    }
}
