package com.duonghoang.shopapp_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "Order id must be greater than 0")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "Product id must be greater than 0")
    private Long productId;

    @Min(value = 0, message = "Product id must be greater than 0")
    private Float price;

    @JsonProperty("number_of_products")
    @Min(value = 1, message = "Number of product must be greater than 0")
    private int numberOfProducts;

    @JsonProperty("total_money")
    @Min(value = 0, message = "Total money of product must be greater than 0")
    private Float totalMoney;

    private String color;
}
