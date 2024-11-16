package com.duonghoang.shopapp_backend.controllers;

import com.duonghoang.shopapp_backend.dtos.CategoryDTO;
import com.duonghoang.shopapp_backend.models.Category;
import com.duonghoang.shopapp_backend.services.category.CategoryService;
import com.duonghoang.shopapp_backend.services.category.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
//@Validated  // debug chưa vao trong method -> class/method level
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> creatCategory(@RequestBody @Valid CategoryDTO categoryDTO,
                                           BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            List<String> errorMessage= bindingResult.getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(errorMessage);
        }
        categoryService.createCategory(categoryDTO);
        return ResponseEntity.status(201).body("Create categories success!");
    }

//    get all categories
    @GetMapping
    public ResponseEntity<?> getAllCategories(){
        List<Category> categories= categoryService.getAllCategories();
        return ResponseEntity.status(200).body(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id,@RequestBody @Valid CategoryDTO dto){
        categoryService.updateCategory(id, dto);
        return ResponseEntity.status(203).body("This is update categories " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) throws Exception {
        categoryService.deleteCategory(id);
        return ResponseEntity.status(203).body("This is delete categories " +id);
    }
}
