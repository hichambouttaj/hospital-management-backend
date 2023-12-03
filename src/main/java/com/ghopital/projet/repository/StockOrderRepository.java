package com.ghopital.projet.repository;

import com.ghopital.projet.entity.StockOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockOrderRepository extends JpaRepository<StockOrder, Long> {
    @Query("select so from StockOrder so where so.code in (" +
            "select d.order.code from DeliveryNote d where d.product.id = :product_id)")
    List<StockOrder> findAllByProduct(@Param("product_id") Long productId);
}