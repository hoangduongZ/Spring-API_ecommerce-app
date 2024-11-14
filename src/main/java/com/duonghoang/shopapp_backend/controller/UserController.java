package com.duonghoang.shopapp_backend.controller;

import com.duonghoang.shopapp_backend.dtos.UserDTO;
import com.duonghoang.shopapp_backend.dtos.UserLoginDTO;
import jakarta.validation.Valid;
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
public class UserController {
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){
        try{
            if (bindingResult.hasErrors()){
                List<String> errorsMessage = bindingResult.getFieldErrors()
                        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessage);
            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())){
                return ResponseEntity.badRequest().body("Password does not match!");
            }
            return ResponseEntity.ok("Register successfully!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<String> errorsMessage = bindingResult.getFieldErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorsMessage);
        }
        return ResponseEntity.ok("Some token");
    }
}
