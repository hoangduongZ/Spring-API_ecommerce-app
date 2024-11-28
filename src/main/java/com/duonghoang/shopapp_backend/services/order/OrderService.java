package com.duonghoang.shopapp_backend.services.order;

import com.duonghoang.shopapp_backend.dtos.CartItemDTO;
import com.duonghoang.shopapp_backend.dtos.OrderDTO;
import com.duonghoang.shopapp_backend.exception.DataNotFoundException;
import com.duonghoang.shopapp_backend.models.Order;
import com.duonghoang.shopapp_backend.models.OrderDetail;
import com.duonghoang.shopapp_backend.models.OrderStatus;
import com.duonghoang.shopapp_backend.models.Product;
import com.duonghoang.shopapp_backend.models.User;
import com.duonghoang.shopapp_backend.repositories.OrderDetailRepository;
import com.duonghoang.shopapp_backend.repositories.OrderRepository;
import com.duonghoang.shopapp_backend.repositories.ProductRepository;
import com.duonghoang.shopapp_backend.repositories.UserRepository;
import com.duonghoang.shopapp_backend.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public OrderResponse createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(()->
                new DataNotFoundException("User not found!")
        );

        modelMapper.typeMap(OrderDTO.class, Order.class)
                        .addMappings(mapper -> mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
//        Check date must be > now
        LocalDate shippingDate = orderDTO.getShippingDate() == null
                ? LocalDate.now(): orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())){
            throw new DataNotFoundException("Shipping date must be at least today!");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO: orderDTO.getCartItems()){
            OrderDetail orderDetail= new OrderDetail();
            orderDetail.setOrder(order);
            long productId = cartItemDTO.getProductId();
            long quantity= cartItemDTO.getQuantity();
            Product product = productRepository.findById(productId).orElseThrow(()-> new DataNotFoundException("Product not found"));
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setTotalMoney(product.getPrice() * quantity);
            orderDetail.setPrice(product.getPrice());
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
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
