package com.bankinc.api.services.customer;

import com.bankinc.api.models.entity.TblCustomer;
import com.bankinc.api.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceimpl customerService;

    private TblCustomer genericCustomer;

    @BeforeEach
    void setUp() {
        genericCustomer = new TblCustomer();
        genericCustomer.setNumIdCustomer(1L);
        genericCustomer.setStrFirstName("pepito");
        genericCustomer.setStrlastName("perez");
        genericCustomer.setStrIdentificationType("CC");
        genericCustomer.setStrIdentificationNumber("123456789");
        genericCustomer.setStrPhone("1234567890");
        genericCustomer.setBirthDate(LocalDateTime.now().minusYears(30));
    }

    @Nested
    @DisplayName("findAll Tests")
    class FindAllTests {

        @Test
        void shouldReturnListOfCustomersWhenCustomersExist() {
            // Arrange
            List<TblCustomer> expectedCustomers = Arrays.asList(
                    genericCustomer,
                    createCustomer(2L, "maria", "martinez")
            );
            when(customerRepository.findAll()).thenReturn(expectedCustomers);

            // Act
            List<TblCustomer> actualCustomers = customerService.finAll();

            // Assert
            assertNotNull(actualCustomers);
            assertEquals(2, actualCustomers.size());
            assertEquals(expectedCustomers, actualCustomers);
            verify(customerRepository, times(1)).findAll();
        }

        @Test
        void shouldReturnEmptyListWhenNoCustomersExist() {
            // Arrange
            when(customerRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<TblCustomer> actualCustomers = customerService.finAll();

            // Assert
            assertNotNull(actualCustomers);
            assertTrue(actualCustomers.isEmpty());
            verify(customerRepository, times(1)).findAll();
        }
    }

    @Nested
    class SaveTests {

        @Test
        void shouldSuccessfullySaveValidCustomer() {
            // Arrange
            when(customerRepository.save(any(TblCustomer.class))).thenReturn(genericCustomer);

            // Act
            TblCustomer savedCustomer = customerService.save(genericCustomer);

            // Assert
            assertNotNull(savedCustomer);
            assertEquals(genericCustomer.getNumIdCustomer(), savedCustomer.getNumIdCustomer());
            assertEquals(genericCustomer.getStrFirstName(), savedCustomer.getStrFirstName());
            verify(customerRepository, times(1)).save(any(TblCustomer.class));
        }

        @Test
        void shouldThrowExceptionWhenSavingNullCustomer() {
            // Arrange
            when(customerRepository.save(null)).thenThrow(IllegalArgumentException.class);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> customerService.save(null));
            verify(customerRepository, times(1)).save(null);
        }
    }

    @Nested
    class FindByIdTests {

        @Test
        void shouldReturnCustomerWhenValidIdExists() {
            // Arrange
            when(customerRepository.findById(1L)).thenReturn(Optional.of(genericCustomer));

            // Act
            Optional<TblCustomer> foundCustomer = customerService.findById(1L);

            // Assert
            assertTrue(foundCustomer.isPresent());
            assertEquals(genericCustomer.getNumIdCustomer(), foundCustomer.get().getNumIdCustomer());
            verify(customerRepository, times(1)).findById(1L);
        }

        @Test
        void shouldReturnEmptyOptionalWhenIdDoesNotExist() {
            // Arrange
            when(customerRepository.findById(999L)).thenReturn(Optional.empty());

            // Act
            Optional<TblCustomer> foundCustomer = customerService.findById(999L);

            // Assert
            assertFalse(foundCustomer.isPresent());
            verify(customerRepository, times(1)).findById(999L);
        }
    }

    @Nested
    class DeleteByIdTests {

        @Test
        void shouldSuccessfullyDeleteExistingCustomer() {
            // Arrange
            doNothing().when(customerRepository).deleteById(1L);

            // Act
            String result = customerService.deleteById(1L);

            // Assert
            assertEquals("cliente eliminado", result);
            verify(customerRepository, times(1)).deleteById(1L);
        }

        @Test
        void shouldHandleDeletionFailureGracefully() {
            // Arrange
            doThrow(new RuntimeException("Database error")).when(customerRepository).deleteById(1L);

            // Act
            String result = customerService.deleteById(1L);

            // Assert
            assertEquals("fallo en la eliminacion", result);
            verify(customerRepository, times(1)).deleteById(1L);
        }
    }

    private TblCustomer createCustomer(Long id, String firstName, String lastName) {
        TblCustomer customer = new TblCustomer();
        customer.setNumIdCustomer(id);
        customer.setStrFirstName(firstName);
        customer.setStrlastName(lastName);
        customer.setStrIdentificationType("CC");
        customer.setStrIdentificationNumber("987654321");
        customer.setStrPhone("9876543210");
        customer.setBirthDate(LocalDateTime.now().minusYears(25));
        return customer;
    }
}