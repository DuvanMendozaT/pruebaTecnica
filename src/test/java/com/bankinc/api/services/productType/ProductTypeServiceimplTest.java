package com.bankinc.api.services.productType;

import com.bankinc.api.models.entity.TblProductType;
import com.bankinc.api.repository.ProductTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductTypeServiceImplTest {

    @Mock
    private ProductTypeRepository productTypeRepository;

    @InjectMocks
    private ProductTypeServiceimpl productTypeService;

    private TblProductType genericProducType;

    @BeforeEach
    void setUp() {
        genericProducType = new TblProductType();
        genericProducType.setNumIdProductType(1);
        genericProducType.setStrProductNumber("123456");
        genericProducType.setStrDescription("tarjeta de debito");
    }

    @Nested
    @DisplayName("finAll Tests")
    class FindAllTests {

        @Test

        void shouldReturnListOfProductTypesWhenTheyExist() {
            // Arrange
            List<TblProductType> expectedProductTypes = Arrays.asList(
                    genericProducType,
                    createProductType(2, "234567", "tarjeta de debito")
            );
            when(productTypeRepository.findAll()).thenReturn(expectedProductTypes);

            // Act
            List<TblProductType> actualProductTypes = productTypeService.finAll();

            // Assert
            assertNotNull(actualProductTypes);
            assertEquals(2, actualProductTypes.size());
            assertEquals(expectedProductTypes, actualProductTypes);
            verify(productTypeRepository, times(1)).findAll();
        }

        @Test
        void shouldReturnEmptyListWhenNoProductTypesExist() {
            // Arrange
            when(productTypeRepository.findAll()).thenReturn(Collections.emptyList());

            // Act
            List<TblProductType> actualProductTypes = productTypeService.finAll();

            // Assert
            assertNotNull(actualProductTypes);
            assertTrue(actualProductTypes.isEmpty());
            verify(productTypeRepository, times(1)).findAll();
        }
    }

    @Nested
    class SaveTests {

        @Test
        void shouldSuccessfullySaveValidProductType() {
            // Arrange
            when(productTypeRepository.save(any(TblProductType.class))).thenReturn(genericProducType);

            // Act
            TblProductType savedProductType = productTypeService.save(genericProducType);

            // Assert
            assertNotNull(savedProductType);
            assertEquals(genericProducType.getNumIdProductType(), savedProductType.getNumIdProductType());
            assertEquals(genericProducType.getStrProductNumber(), savedProductType.getStrProductNumber());
            assertEquals(genericProducType.getStrDescription(), savedProductType.getStrDescription());
            verify(productTypeRepository, times(1)).save(any(TblProductType.class));
        }

        @Test
        void shouldThrowExceptionWhenSavingNullProductType() {
            // Arrange
            when(productTypeRepository.save(null)).thenThrow(IllegalArgumentException.class);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> productTypeService.save(null));
            verify(productTypeRepository, times(1)).save(null);
        }
    }

    @Nested
    class FindByIdTests {

        @Test
        void shouldReturnEmptyOptionalForAnyId() {
            // Act
            Optional<TblProductType> result = productTypeService.findById(1);

            // Assert
            assertFalse(result.isPresent());
            verify(productTypeRepository, never()).findById(anyInt());
        }
    }

    @Nested
    class DeleteByIdTests {

        @Test
        void shouldSuccessfullyDeleteExistingProductType() {
            // Arrange
            doNothing().when(productTypeRepository).deleteById(1);

            // Act
            String result = productTypeService.deleteById(1);

            // Assert
            assertEquals("tipo de producto elimiando", result);
            verify(productTypeRepository, times(1)).deleteById(1);
        }

        @Test
        void shouldHandleDeletionFailureGracefully() {
            // Arrange
            doThrow(new RuntimeException("Error de base de datos")).when(productTypeRepository).deleteById(1);

            // Act
            String result = productTypeService.deleteById(1);

            // Assert
            assertEquals("erroe en la eliminacion", result);
            verify(productTypeRepository, times(1)).deleteById(1);
        }
    }

    private TblProductType createProductType(int id, String productNumber, String description) {
        TblProductType productType = new TblProductType();
        productType.setNumIdProductType(id);
        productType.setStrProductNumber(productNumber);
        productType.setStrDescription(description);
        return productType;
    }
}