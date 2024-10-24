package com.bankinc.api.services.productType;

import com.bankinc.api.models.dto.ProductTypeDto;
import com.bankinc.api.models.entity.TblProductType;
import com.bankinc.api.models.mappers.ProductTypeMapper;
import com.bankinc.api.repository.ProductTypeRepository;
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

class ProductTypeServiceimplTest {
    @InjectMocks
    private ProductTypeServiceimpl productTypeService;

    @Mock
    private ProductTypeRepository productTypeRepository;

    @Mock
    private ProductTypeMapper productTypeMapper;

    private TblProductType tblProductType;
    private ProductTypeDto productTypeDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        tblProductType = new TblProductType();
        tblProductType.setNumIdProductType(1);

        productTypeDto = new ProductTypeDto();
        productTypeDto.setNumIdProductType(1);
    }

    @Test
    public void testFindAll() {
        List<TblProductType> productTypeList = new ArrayList<>();
        productTypeList.add(tblProductType);

        when(productTypeRepository.findAll()).thenReturn(productTypeList);
        when(productTypeMapper.toDtos(productTypeList)).thenReturn(List.of(productTypeDto));

        List<ProductTypeDto> result = productTypeService.findAll();

        assertEquals(1, result.size());
        assertEquals(productTypeDto, result.get(0));
        verify(productTypeRepository, times(1)).findAll();
    }

    @Test
    public void testSave() {
        when(productTypeRepository.save(tblProductType)).thenReturn(tblProductType);
        when(productTypeMapper.toDto(tblProductType)).thenReturn(productTypeDto);

        ProductTypeDto result = productTypeService.save(tblProductType);

        assertEquals(productTypeDto, result);
        verify(productTypeRepository, times(1)).save(tblProductType);
    }

    @Test
    public void testFindById_Found() {
        when(productTypeRepository.findById(1)).thenReturn(Optional.of(tblProductType));
        when(productTypeMapper.toDto(tblProductType)).thenReturn(productTypeDto);

        Optional<ProductTypeDto> result = productTypeService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(productTypeDto, result.get());
        verify(productTypeRepository, times(1)).findById(1);
    }

    @Test
    public void testFindById_NotFound() {
        when(productTypeRepository.findById(1)).thenReturn(Optional.empty());

        Optional<ProductTypeDto> result = productTypeService.findById(1);

        assertFalse(result.isPresent());
        verify(productTypeRepository, times(1)).findById(1);
    }

    @Test
    public void testDeleteById_Exists() {
        when(productTypeRepository.existsById(1)).thenReturn(true);

        String result = productTypeService.deleteById(1);

        assertEquals("Cliente eliminado con éxito", result);
        verify(productTypeRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDeleteById_NotFound() {
        when(productTypeRepository.existsById(1)).thenReturn(false);

        String result = productTypeService.deleteById(1);

        assertEquals("Cliente no encontrado", result);
        verify(productTypeRepository, never()).deleteById(1);
    }

    @Test
    public void testDeleteById_Failure() {
        when(productTypeRepository.existsById(1)).thenReturn(true);
        doThrow(new DataAccessException("Database error") {}).when(productTypeRepository).deleteById(1);

        String result = productTypeService.deleteById(1);

        assertEquals("Fallo en la eliminación: Database error", result);
    }

}