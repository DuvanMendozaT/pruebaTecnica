package com.bankinc.api.repository;
import com.bankinc.api.models.entity.TblProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<TblProductType, Integer> {
}