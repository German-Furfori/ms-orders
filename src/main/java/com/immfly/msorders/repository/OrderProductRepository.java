package com.immfly.msorders.repository;

import com.immfly.msorders.entity.OrderProduct;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderProduct OP WHERE OP.id = :id")
    void deleteByProductOrderId(@Param("id") Long id);

}
