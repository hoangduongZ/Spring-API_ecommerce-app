package com.duonghoang.shopapp_backend.services.category;

import com.duonghoang.shopapp_backend.dtos.CategoryDTO;
import com.duonghoang.shopapp_backend.models.Category;
import com.duonghoang.shopapp_backend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryDTO dto) {
        Category category = Category.builder().name(dto.getName()).build();
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(()-> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long categoryId, CategoryDTO category) {
        Category existingCategory= getCategoryById(categoryId);
        existingCategory.setName(category.getName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public Category deleteCategory(long id) throws Exception {
        categoryRepository.deleteById(id);
        return null;
    }
}
