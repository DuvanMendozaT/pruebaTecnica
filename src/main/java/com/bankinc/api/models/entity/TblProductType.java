package com.bankinc.api.models.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "TBL_PRODUCT_TYPES")
public class TblProductType {
    @Id
    @Column(name = "NUM_ID_PRODUCT_TYPE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_types_seq_gen")
    @SequenceGenerator(name = "product_types_seq_gen", sequenceName = "TBL_PRODUCT_TYPES_SEQ", allocationSize = 1)
    private int numIdProductType;

    @Column(name = "STR_PRODUCT_NUMBER", nullable = false, unique = true, length = 6)
    private String strProductNumber;

    @Column(name = "STR_PRODUCT_DESCRIPTION", nullable = false, length = 100)
    private String strDescription;
}