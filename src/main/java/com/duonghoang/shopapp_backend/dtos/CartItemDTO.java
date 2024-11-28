package com.duonghoang.shopapp_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CartItemDTO {
    @JsonProperty("product_id")
    private long productId;

    @JsonProperty("quantity")
    private long quantity;
}
