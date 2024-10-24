package com.bankinc.api.services.productType;


import com.bankinc.api.models.dto.ProductTypeDto;
import com.bankinc.api.models.entity.TblProductType;
import java.util.List;
import java.util.Optional;

public interface ProductTypeService {
    List<ProductTypeDto> findAll();
    ProductTypeDto save(TblProductType tblProductType);
    Optional<ProductTypeDto> findById(int id);
    String deleteById(int id);
}
