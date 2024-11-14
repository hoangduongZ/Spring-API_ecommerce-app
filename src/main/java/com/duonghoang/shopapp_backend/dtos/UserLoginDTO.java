package com.duonghoang.shopapp_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required!")
    private String phoneNumber;

    @NotBlank(message = "Password is required!")
    private String password;


}
