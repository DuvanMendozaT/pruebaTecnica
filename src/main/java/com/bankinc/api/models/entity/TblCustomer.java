package com.bankinc.api.models.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "TBL_CUSTOMER")
public class TblCustomer {

        @Id
        @Column(name = "NUM_ID_CUSTOMER")
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq_gen")
        @SequenceGenerator(name = "customer_seq_gen", sequenceName = "TBL_CUSTOMER_SEQ", allocationSize = 1)
        private Long numIdCustomer;

        @Column(name = "STR_FIRST_NAME", nullable = false, length = 50)
        private String strFirstName;

        @Column(name = "STR_LAST_NAME", length = 50)
        private String strlastName;

        @Column(name = "STR_IDENTIFICATION_TYPE", nullable = false, length = 10)
        private String strIdentificationType;

        @Column(name = "STR_IDENTIFICATION_NUMBER", nullable = false, length = 20)
        private String strIdentificationNumber;

        @Column(name = "STR_PHONE", nullable = false, length = 20)
        private String strPhone;

        @Column(name = "DTM_BIRTHDATE", nullable = false)
        private LocalDateTime birthDate;

        @OneToMany(mappedBy = "numIdCustomer")
        private List<TblProducts> products;
    }

