package com.duonghoang.shopapp_backend.controllers;

import com.duonghoang.shopapp_backend.component.LocalizationUtil;
import com.duonghoang.shopapp_backend.dtos.UserDTO;
import com.duonghoang.shopapp_backend.dtos.UserLoginDTO;
import com.duonghoang.shopapp_backend.models.User;
import com.duonghoang.shopapp_backend.responses.LoginResponse;
import com.duonghoang.shopapp_backend.responses.RegisterResponse;
import com.duonghoang.shopapp_backend.services.user.IUserService;
import com.duonghoang.shopapp_backend.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtil localizationUtils;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorsMessage = bindingResult.getFieldErrors()
                        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessage);
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body(RegisterResponse.builder().message(
                                localizationUtils.getLocalizedMessage(MessageKeys.PASSWORD_NOT_MATCH)).build()
                );
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO
            , BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorsMessage = bindingResult.getFieldErrors()
                        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessage);
            }
            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            return ResponseEntity.ok(LoginResponse.builder().message(
                            localizationUtils.getLocalizedMessage(
                                    MessageKeys.LOGIN_SUCCESSFULLY))
                    .token(token).build());
        } catch (Exception ex) {
            return ResponseEntity.ok(LoginResponse.builder().message(
                            localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_FAILED, ex.getMessage()))
                    .build());
        }
    }
}
