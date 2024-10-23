package com.bankinc.api.repository;

import com.bankinc.api.models.entity.TblCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<TblCustomer, Long> {
}
