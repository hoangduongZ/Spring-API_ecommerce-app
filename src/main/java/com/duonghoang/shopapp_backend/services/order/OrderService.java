package com.duonghoang.shopapp_backend.services.order;

import com.duonghoang.shopapp_backend.dtos.OrderDTO;
import com.duonghoang.shopapp_backend.exception.DataNotFoundException;
import com.duonghoang.shopapp_backend.models.Order;
import com.duonghoang.shopapp_backend.models.User;
import com.duonghoang.shopapp_backend.repositories.OrderRepository;
import com.duonghoang.shopapp_backend.repositories.UserRepository;
import com.duonghoang.shopapp_backend.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(()->
                new DataNotFoundException("User not found!")
        );
        orderRepository.save(Order.builder()
                .user(user)
                .email(orderDTO.getEmail())
                .address(orderDTO.getAddress())
                .fullName(orderDTO.getFullName())
                .phoneNumber(orderDTO.getPhoneNumber())
                .build());
        return null;
    }

    @Override
    public OrderResponse getOrder(Long id) {
        return null;
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO dto) {
        return null;
    }

    @Override
    public void deleteOrder(Long id) {

    }

    @Override
    public List<OrderResponse> getAllOrders(Long userId) {
        return List.of();
    }
}
