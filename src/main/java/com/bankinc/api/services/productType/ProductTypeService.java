package com.bankinc.api.services.productType;


import com.bankinc.api.models.entity.TblProductType;
import java.util.List;
import java.util.Optional;

public interface ProductTypeService {
    public List<TblProductType> finAll();
    public TblProductType save(TblProductType tblProductType);
    public Optional<TblProductType> findById(int id);
    public String deleteById(int id);
}
