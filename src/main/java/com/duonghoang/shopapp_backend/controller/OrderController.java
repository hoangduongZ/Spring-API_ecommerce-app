package com.duonghoang.shopapp_backend.controller;

import com.duonghoang.shopapp_backend.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO, BindingResult bindingResult){
        try{
            if (bindingResult.hasErrors()){
                List<String> errorsMessage = bindingResult.getFieldErrors()
                        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorsMessage);
            }
            return ResponseEntity.ok("Create order successfully!" + orderDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
        try{
            return ResponseEntity.ok("Get order list for user id: "+ userId);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrders(@Valid @PathVariable("id") Long id, @RequestBody @Valid OrderDTO orderDTO){
        try{
            return ResponseEntity.ok("Update order");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable("id") Long id){
        try{
//            Xoa mem khong xoa han: active = false
            return ResponseEntity.ok("Delete order successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
