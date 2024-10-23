package com.bankinc.api.services.productType;

import com.bankinc.api.models.entity.TblProductType;
import com.bankinc.api.repository.ProductTypeRepository;
import com.bankinc.api.services.productType.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class ProductTypeServiceimpl implements ProductTypeService {
    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Override
    public List<TblProductType> finAll() {
        return productTypeRepository.findAll();
    }

    @Override
    public TblProductType save(TblProductType tblProductType) {
        return productTypeRepository.save(tblProductType);
    }

    @Override
    public Optional<TblProductType> findById(int id) {
        return Optional.empty();
    }

    @Override
    public String deleteById(int id) {
        try {
            productTypeRepository.deleteById(id);
            return "cliente eliminado";
        }catch (Exception e){
            return "fallo en la eliminacion";
        }
    }
}
