package com.duonghoang.shopapp_backend.services.order_detail;

import com.duonghoang.shopapp_backend.dtos.OrderDetailDTO;
import com.duonghoang.shopapp_backend.exception.DataNotFoundException;
import com.duonghoang.shopapp_backend.models.Order;
import com.duonghoang.shopapp_backend.models.OrderDetail;
import com.duonghoang.shopapp_backend.models.Product;
import com.duonghoang.shopapp_backend.repositories.OrderDetailRepository;
import com.duonghoang.shopapp_backend.repositories.OrderRepository;
import com.duonghoang.shopapp_backend.repositories.ProductRepository;
import com.duonghoang.shopapp_backend.responses.OrderDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO dto) throws Exception {
        Order order = orderRepository.findById(dto.getOrderId()).orElseThrow(()
        -> new DataNotFoundException("Order not found!, id: "+ dto.getOrderId()));

        Product product = productRepository.findById(dto.getProductId()).orElseThrow(()
        -> new DataNotFoundException("Product not found, id: "+ dto.getProductId()));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(dto.getNumberOfProducts())
                .totalMoney(dto.getTotalMoney())
                .color(dto.getColor())
                .price(dto.getPrice())
                .build();
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) {
        return orderDetailRepository.findById(id).orElseThrow(()
        -> new DataNotFoundException("Order detail not found, id: "+ id));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
        Optional<OrderDetail> optionalOrderDetail = orderDetailRepository.findById(id);
        if (optionalOrderDetail.isPresent()){
            OrderDetail existingOrderDetail = optionalOrderDetail.get();
            Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(
                    ()-> new DataNotFoundException("Order not found!, id: "+ orderDetailDTO.getOrderId())
            );
            existingOrderDetail.setOrder(order);
            Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(
                    () -> new DataNotFoundException("Product not found exception!")
            );
            existingOrderDetail.setProduct(product);
            existingOrderDetail.setPrice(orderDetailDTO.getPrice());
            if (orderDetailDTO.getNumberOfProducts() == 0){
                orderDetailRepository.delete(existingOrderDetail);
                return existingOrderDetail;
            }
            existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
            existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
            existingOrderDetail.setColor(orderDetailDTO.getColor());
            return orderDetailRepository.save(existingOrderDetail);
        }else{
            throw new DataNotFoundException("Order detail not found, id: "+ id);
        }
    }

    @Override
    public void deleteOrderDetail(Long id) {
        OrderDetail orderDetail = getOrderDetail(id);
        orderDetailRepository.delete(orderDetail);
    }

    @Override
    public List<OrderDetail> getOrderDetails(Long orderId) {
        orderRepository.findById(orderId).orElseThrow(()
        -> new DataNotFoundException("Order not found!, id: "+ orderId));
        return orderDetailRepository.findByOrderId(orderId);
    }
}
