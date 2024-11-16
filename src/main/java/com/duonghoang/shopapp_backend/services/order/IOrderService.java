package com.duonghoang.shopapp_backend.services.order;

import com.duonghoang.shopapp_backend.dtos.OrderDTO;
import com.duonghoang.shopapp_backend.responses.OrderResponse;

import java.util.List;

public interface IOrderService {
    OrderResponse createOrder(OrderDTO orderDTO) throws Exception;
    OrderResponse getOrder(Long id);
    OrderResponse updateOrder(Long id, OrderDTO dto);
    void deleteOrder(Long id);
    List<OrderResponse> getAllOrders(Long userId);
}
