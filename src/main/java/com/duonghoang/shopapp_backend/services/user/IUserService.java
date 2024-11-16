package com.duonghoang.shopapp_backend.services.user;

import com.duonghoang.shopapp_backend.dtos.UserDTO;
import com.duonghoang.shopapp_backend.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO);

    String login(String phoneNumber, String password);
}
