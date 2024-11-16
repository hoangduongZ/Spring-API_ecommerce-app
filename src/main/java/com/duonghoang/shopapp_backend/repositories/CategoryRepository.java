package com.duonghoang.shopapp_backend.repositories;

import com.duonghoang.shopapp_backend.models.Category;
import com.duonghoang.shopapp_backend.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
//    List<OrderDetail> findByOrderId(long orderId);
}
