package com.bankinc.api.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBL_PRODUCTS")
public class TblProducts {

    @Id
    @Column(name = "NUM_ID_PRODUCT")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq_gen")
    @SequenceGenerator(name = "products_seq_gen", sequenceName = "TBL_PRODUCTS_SEQ", allocationSize = 1)
    private long numIdProduct;

    @Column(name = "STR_PRODUCT_NUMBER", nullable = false, unique = true, length = 16)
    private String strProductNumber;

    @ManyToOne
    @JoinColumn(name = "NUM_ID_PRODUCT_TYPE", nullable = false)
    private TblProductType numIdProducType;

    @Column(name = "DTM_EXPIRATION_DATE", nullable = false)
    private LocalDateTime dtmExpirationDate;

    @Column(name = "DTM_CREATION_DATE", nullable = false)
    private LocalDateTime dtmCreationDate;

    @Column(name = "NUM_BALANCE", nullable = false)
    private Double numbalance;

    @ManyToOne
    @JoinColumn(name = "NUM_ID_CUSTOMER", nullable = false)
    private TblCustomer numIdCustomer;

    @Column(name = "NUM_ACTIVATION")
    private int numActivation;

    @Column(name = "NUM_STATUS")
    private long numStatus;

    @PrePersist
    protected void onCreate() {
        dtmCreationDate = LocalDateTime.now();
        dtmExpirationDate = dtmCreationDate.plusYears(3);
        numbalance = 0.0;
        numActivation = 0;
        numStatus = 1;
    }

}
