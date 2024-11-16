package com.duonghoang.shopapp_backend.services.category;

import com.duonghoang.shopapp_backend.dtos.CategoryDTO;
import com.duonghoang.shopapp_backend.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO category);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId, CategoryDTO category);
    Category deleteCategory(long id) throws Exception;
}
