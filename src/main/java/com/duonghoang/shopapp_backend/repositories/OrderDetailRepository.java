package com.duonghoang.shopapp_backend.repositories;

import com.duonghoang.shopapp_backend.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
