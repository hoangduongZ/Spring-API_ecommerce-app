package com.duonghoang.shopapp_backend.responses;

import com.duonghoang.shopapp_backend.models.Order;
import com.duonghoang.shopapp_backend.models.OrderDetail;
import com.duonghoang.shopapp_backend.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class OrderDetailResponse {
    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;

    private Float price;

    @JsonProperty("number_of_products")
    private long numberOfProducts;

    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail){
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .color(orderDetail.getColor())
                .price(orderDetail.getPrice())
                .totalMoney(orderDetail.getTotalMoney())
                .build();
    }
}
