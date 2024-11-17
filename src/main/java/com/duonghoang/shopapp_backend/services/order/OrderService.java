package com.duonghoang.shopapp_backend.services.order;

import com.duonghoang.shopapp_backend.dtos.OrderDTO;
import com.duonghoang.shopapp_backend.exception.DataNotFoundException;
import com.duonghoang.shopapp_backend.models.Order;
import com.duonghoang.shopapp_backend.models.OrderStatus;
import com.duonghoang.shopapp_backend.models.User;
import com.duonghoang.shopapp_backend.repositories.OrderRepository;
import com.duonghoang.shopapp_backend.repositories.UserRepository;
import com.duonghoang.shopapp_backend.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponse createOrder(OrderDTO orderDTO) {
        Order order = new Order();
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(()->
                new DataNotFoundException("User not found!")
        );

        modelMapper.typeMap(OrderDTO.class, Order.class)
                        .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
//        Check date must be > now
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now(): orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Shipping date must be at least today!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
//        OrderResponse orderResponse = new OrderResponse();
//        modelMapper.typeMap(Order.class, OrderResponse.class);
//        modelMapper.map(order, orderResponse);
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse getOrder(Long id) {
        Order order= orderRepository.findById(id).orElseThrow(()
        -> new DataNotFoundException("Order not found!"));
        return modelMapper.map(order, OrderResponse.class);
    }

    @Override
    public OrderResponse updateOrder(Long id, OrderDTO dto) {
        Order orderExisting = orderRepository.findById(id).orElseThrow(()
        -> new DataNotFoundException("Order not found!"));
        User existingUser = userRepository.findById(dto.getUserId()).orElseThrow(
                ()-> new DataNotFoundException("User not found!")
        );
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        modelMapper.map(dto, orderExisting);
        orderExisting.setUser(existingUser);
        orderRepository.save(orderExisting);
        return modelMapper.map(orderExisting, OrderResponse.class);
    }

    @Override
    public void deleteOrder(Long id) {
        Order orderExisting = orderRepository.findById(id).orElseThrow(()
                -> new DataNotFoundException("Order not found!"));
        if (orderExisting != null){
            orderExisting.setActive(false);
            orderRepository.save(orderExisting);
        }
    }

    @Override
    public List<OrderResponse> getAllOrders(Long userId) {
        userRepository.findById(userId).orElseThrow(()-> new DataNotFoundException("User not found!"));
        return orderRepository.findByUserId(userId)
                .stream()
                .map((order)->
                        modelMapper.map(order, OrderResponse.class))
                .toList();
    }
}
