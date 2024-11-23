package com.duonghoang.shopapp_backend.services.user;

import com.duonghoang.shopapp_backend.dtos.UserDTO;
import com.duonghoang.shopapp_backend.exception.DataNotFoundException;
import com.duonghoang.shopapp_backend.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;

    String login(String phoneNumber, String password) throws Exception;
}
