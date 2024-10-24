package com.bankinc.api.services.productType;

import com.bankinc.api.models.dto.ProductTypeDto;
import com.bankinc.api.models.entity.TblProductType;
import com.bankinc.api.models.mappers.ProductTypeMapper;
import com.bankinc.api.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ProductTypeServiceimpl implements ProductTypeService {
    @Autowired
    private ProductTypeRepository productTypeRepository;
    @Autowired
    private ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductTypeDto> findAll() {
        return productTypeMapper.toDtos(productTypeRepository.findAll());
    }

    @Override
    public ProductTypeDto save(TblProductType tblProductType) {
        return productTypeMapper.toDto(productTypeRepository.save(tblProductType));
    }

    @Override
    public Optional<ProductTypeDto> findById(int id) {
        return productTypeRepository.findById(id)
                .map(tblProductType -> productTypeMapper.toDto(tblProductType));
    }

    @Override
    public String deleteById(int id) {
        if(productTypeRepository.existsById(id)){
            try {
                productTypeRepository.deleteById(id);
                return "Cliente eliminado con éxito";
            } catch (DataAccessException e) {
                return "Fallo en la eliminación: " + e.getMessage();
            }
        } else {
            return "Cliente no encontrado";
        }
    }
}
