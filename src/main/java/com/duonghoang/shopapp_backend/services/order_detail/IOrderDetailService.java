package com.duonghoang.shopapp_backend.services.order_detail;

import com.duonghoang.shopapp_backend.dtos.OrderDetailDTO;
import com.duonghoang.shopapp_backend.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO dto) throws Exception;
    OrderDetail getOrderDetail(Long id);
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO);
    void deleteOrderDetail(Long id);
    List<OrderDetail> getOrderDetails(Long orderId);
}
