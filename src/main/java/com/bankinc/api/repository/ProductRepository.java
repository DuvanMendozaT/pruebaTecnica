package com.bankinc.api.repository;

import com.bankinc.api.models.entity.TblProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<TblProducts, Long> {
}