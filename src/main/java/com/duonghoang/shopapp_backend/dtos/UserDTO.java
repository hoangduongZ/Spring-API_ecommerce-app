package com.duonghoang.shopapp_backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty("fullname")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "Phone number is required!")
    private String phoneNumber;

    private String address;

    @NotBlank(message = "Password is required!")
    private String password;

    @JsonProperty("retype_password")
    private String retypePassword;

    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private String faceBookAccountId;

    @JsonProperty("google_account_id")
    private String googleAccountId;

    @JsonProperty("role_id")
    @NotNull(message = "role id is required!")
    private Long roleId;
}
