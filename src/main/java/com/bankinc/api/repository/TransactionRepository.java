package com.bankinc.api.repository;

import com.bankinc.api.models.entity.TblTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TblTransaction, Long> {
}
