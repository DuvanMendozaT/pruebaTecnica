package com.bankinc.api.models.mappers;

import com.bankinc.api.models.dto.TransactionDto;
import com.bankinc.api.models.entity.TblTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    CustomerMapper CUSTOMER_MAPPER = Mappers.getMapper(CustomerMapper.class);
    TransactionDto toDto(TblTransaction tblTransaction);
    TblTransaction toEntity(TransactionDto transactionDto);
}