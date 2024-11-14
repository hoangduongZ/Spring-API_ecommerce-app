package com.duonghoang.shopapp_backend.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {
    @NotBlank(message = "Category name is not empty")
    private String name;
}
