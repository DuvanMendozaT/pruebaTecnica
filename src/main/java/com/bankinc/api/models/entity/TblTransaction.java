package com.bankinc.api.models.entity;
import com.bankinc.api.commmon.Constant;
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
@Table(name = "TBL_TRANSACTIONS")
public class TblTransaction {
    @Id
    @Column(name = "NUM_ID_TRANSACTION")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq_gen")
    @SequenceGenerator(name = "transaction_seq_gen", sequenceName = "TBL_TRANSACTION_SEQ", allocationSize = 1)
    private Long numIdTransaction;

    @Column(name = "NUM_AMOUNT", nullable = false)
    private Double numAmount;

    @Column(name = "DTM_TRANSACTION_DATE", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "STR_STATUS")
    private String strStatus;

    @ManyToOne
    @JoinColumn(name = "NUM_ID_PRODUCT", nullable = false)
    private TblProducts numIdProduct;

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
        strStatus = Constant.SUCESS_STATUS;
    }
}