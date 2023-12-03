package com.ghopital.projet.repository;

import com.ghopital.projet.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {
    @Query("select po from ProductOrder po where po.code in (" +
            "select d.order.code from DeliveryNote d where d.product.id = :product_id)")
    List<ProductOrder> findAllByProduct(@Param("product_id") Long productId);
}