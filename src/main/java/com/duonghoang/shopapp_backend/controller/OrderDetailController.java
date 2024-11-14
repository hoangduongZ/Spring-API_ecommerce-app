package com.duonghoang.shopapp_backend.controller;

import com.duonghoang.shopapp_backend.dtos.OrderDetailDTO;
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
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    @PostMapping
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO
            , BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<String> errorsMessage = bindingResult.getFieldErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorsMessage);
        }
        return ResponseEntity.ok().body("Create order detail here!");
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id){
        return ResponseEntity.ok().body("Get order detail with Id "+ id);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long id){
        return ResponseEntity.ok().body("Get order detail with orderId "+ id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,
                                               @Valid @RequestBody OrderDetailDTO orderDetailDTO,
                                               BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<String> errorsMessage = bindingResult.getFieldErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorsMessage);
        }
        return ResponseEntity.ok("update order details! with id: "+ id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@Valid @PathVariable("id") Long id){
        return ResponseEntity.noContent().build();
    }
}
