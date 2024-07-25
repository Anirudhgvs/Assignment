package com.Assignment.repo;

import com.Assignment.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {

    @Query("SELECT p FROM Product p WHERE p.stock > 0 AND p.supplier = :supplierName")
    Page<Product> productsInStockBySupplier(@Param("supplierName") String supplierName, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.exp > :today AND p.supplier IN :suppliers")
    Page<Product> productsNotExpiredBySupplier(@Param("today") Date today, @Param("suppliers") List<String> suppliers, Pageable pageable);
}
