package com.bankinc.api.models.mappers;

import com.bankinc.api.models.dto.CustomerDto;
import com.bankinc.api.models.entity.TblCustomer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper CUSTOMER_MAPPER = Mappers.getMapper(CustomerMapper.class);

    CustomerDto toDto(TblCustomer tblCustomer);

    TblCustomer toEntity(CustomerDto customerDto);

    List<CustomerDto> toDtos(List<TblCustomer> customers);

    List<TblCustomer> toEntities(List<CustomerDto> customerDTOs);
}
