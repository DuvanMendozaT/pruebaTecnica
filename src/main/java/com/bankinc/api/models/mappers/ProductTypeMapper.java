package com.bankinc.api.models.mappers;

import com.bankinc.api.models.dto.ProductTypeDto;

import com.bankinc.api.models.entity.TblProductType;
import org.mapstruct.Mapper;

import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {
    CustomerMapper CUSTOMER_MAPPER = Mappers.getMapper(CustomerMapper.class);

    ProductTypeDto toDto(TblProductType tblProductType);

    TblProductType toEntity(ProductTypeDto productTypeDto);
    List<ProductTypeDto> toDtos(List<TblProductType> tblProductTypes);

    List<TblProductType> toEntities(List<ProductTypeDto> productTypeDtos);
}
