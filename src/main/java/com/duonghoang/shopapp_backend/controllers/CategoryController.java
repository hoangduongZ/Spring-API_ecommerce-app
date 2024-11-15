package com.duonghoang.shopapp_backend.controllers;

import com.duonghoang.shopapp_backend.dtos.CategoryDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated  // debug chưa vao trong method -> class/method level
public class CategoryController {
//    get all categories
    @GetMapping
    public ResponseEntity<?> getAllCategories(@RequestParam("page") int page,
                                              @RequestParam("limit") int limit){
        return ResponseEntity.status(200).body("hihi");
    }

    @PostMapping
    public ResponseEntity<?> creatCategory(@RequestBody @Valid CategoryDTO categoryDTO,
                                           BindingResult bindingResult){
        if (bindingResult.hasErrors()){
           List<String> errorMessage= bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
           return ResponseEntity.badRequest().body(errorMessage);
        }
        return ResponseEntity.status(201).body("This is create categories");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id){
        return ResponseEntity.status(203).body("This is update categories " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
        return ResponseEntity.status(203).body("This is delete categories " +id);
    }
}
