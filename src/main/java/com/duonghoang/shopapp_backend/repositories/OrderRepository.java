package com.duonghoang.shopapp_backend.repositories;

import com.duonghoang.shopapp_backend.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
}
