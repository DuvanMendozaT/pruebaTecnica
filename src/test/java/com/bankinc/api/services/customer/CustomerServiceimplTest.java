package com.bankinc.api.services.customer;

import com.bankinc.api.models.dto.CustomerDto;
import com.bankinc.api.models.entity.TblCustomer;
import com.bankinc.api.models.mappers.CustomerMapper;
import com.bankinc.api.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class CustomerServiceimplTest {
    @InjectMocks
    private CustomerServiceimpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    private TblCustomer customer;
    private CustomerDto customerDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new TblCustomer();
        customer.setNumIdCustomer(1L);
        customerDto = new CustomerDto();
        customerDto.setNumIdCustomer(1L);
    }

    @Test
    public void testFindAll() {
        List<TblCustomer> customers = new ArrayList<>();
        customers.add(customer);

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toDtos(customers)).thenReturn(List.of(customerDto));

        List<CustomerDto> result = customerService.findAll();

        assertEquals(1, result.size());
        assertEquals(customerDto, result.get(0));
        verify(customerRepository, times(1)).findAll();
        verify(customerMapper, times(1)).toDtos(customers);
    }

    @Test
    public void testSave() {
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        CustomerDto result = customerService.save(customer);

        assertEquals(customerDto, result);
        verify(customerRepository, times(1)).save(customer);
        verify(customerMapper, times(1)).toDto(customer);
    }

    @Test
    public void testFindById_Found() {
        when(customerRepository.findById(customer.getNumIdCustomer())).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        Optional<CustomerDto> result = customerService.findById(customer.getNumIdCustomer());

        assertTrue(result.isPresent());
        assertEquals(customerDto, result.get());
        verify(customerRepository, times(1)).findById(customer.getNumIdCustomer());
        verify(customerMapper, times(1)).toDto(customer);
    }

    @Test
    public void testFindById_NotFound() {
        when(customerRepository.findById(customer.getNumIdCustomer())).thenReturn(Optional.empty());

        Optional<CustomerDto> result = customerService.findById(customer.getNumIdCustomer());

        assertFalse(result.isPresent());
        verify(customerRepository, times(1)).findById(customer.getNumIdCustomer());
    }

    @Test
    public void testDeleteById_Success() {
        when(customerRepository.existsById(customer.getNumIdCustomer())).thenReturn(true);

        String result = customerService.deleteById(customer.getNumIdCustomer());

        assertEquals("Cliente eliminado con éxito", result);
        verify(customerRepository, times(1)).deleteById(customer.getNumIdCustomer());
    }

    @Test
    public void testDeleteById_NotFound() {
        when(customerRepository.existsById(customer.getNumIdCustomer())).thenReturn(false);

        String result = customerService.deleteById(customer.getNumIdCustomer());

        assertEquals("Cliente no encontrado", result);
        verify(customerRepository, never()).deleteById(customer.getNumIdCustomer());
    }

    @Test
    public void testDeleteById_DataAccessException() {
        when(customerRepository.existsById(customer.getNumIdCustomer())).thenReturn(true);
        doThrow(new DataAccessException("Error de acceso a datos") {}).when(customerRepository).deleteById(customer.getNumIdCustomer());

        String result = customerService.deleteById(customer.getNumIdCustomer());

        assertTrue(result.startsWith("Fallo en la eliminación"));
        verify(customerRepository, times(1)).deleteById(customer.getNumIdCustomer());
    }
}