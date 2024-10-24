package com.bankinc.api.models.mappers;

import com.bankinc.api.models.dto.ProductDto;
import com.bankinc.api.models.entity.TblProducts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    CustomerMapper CUSTOMER_MAPPER = Mappers.getMapper(CustomerMapper.class);
    ProductDto toDto(TblProducts tblProducts);
    TblProducts toEntity(ProductDto productDto);
}