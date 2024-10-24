package com.bankinc.api.models.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CustomerDto {

    private Long numIdCustomer;

    private String strFirstName;

    private String strlastName;

    private String strIdentificationType;

    private String strIdentificationNumber;

    private String strPhone;

    private LocalDateTime birthDate;

}
