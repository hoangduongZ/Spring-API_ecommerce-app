package com.duonghoang.shopapp_backend.responses;

import com.duonghoang.shopapp_backend.models.Role;
import com.duonghoang.shopapp_backend.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private long id;
    private String fullName;
    private String phoneNumber;
    private String address;
    private boolean active;
    private Date dateOfBirth;
    private String facebookAccountId;
    private String googleAccountId;
    private Role role;

    public static UserResponse fromUser(User user){
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .active(user.isActive())
                .dateOfBirth(user.getDateOfBirth())
                .facebookAccountId(user.getFacebookAccountId())
                .googleAccountId(user.getGoogleAccountId())
                .role(user.getRole())
                .build();
    }
}
