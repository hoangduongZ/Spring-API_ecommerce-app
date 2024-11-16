package com.duonghoang.shopapp_backend.services.product;

import com.duonghoang.shopapp_backend.dtos.ProductDTO;
import com.duonghoang.shopapp_backend.dtos.ProductImageDTO;
import com.duonghoang.shopapp_backend.exception.DataNotFoundException;
import com.duonghoang.shopapp_backend.exception.InvalidParamException;
import com.duonghoang.shopapp_backend.models.Category;
import com.duonghoang.shopapp_backend.models.Product;
import com.duonghoang.shopapp_backend.models.ProductImage;
import com.duonghoang.shopapp_backend.repositories.CategoryRepository;
import com.duonghoang.shopapp_backend.repositories.ProductImageRepository;
import com.duonghoang.shopapp_backend.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() ->
                new DataNotFoundException("Category not found!"));
        Product product = Product.builder().name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(category).build();
        return productRepository.save(product);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(()
        -> new DataNotFoundException("Cannot find product with id = "+productId));
    }

    @Override
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    @Override
    public Product updateProduct(long id, ProductDTO dto) {
        Product existingProduct= getProductById(id);
        if (existingProduct != null){
            Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow(() ->
                    new DataNotFoundException("Category not found!"));
            existingProduct.setName(dto.getName());
            existingProduct.setCategory(category);
            existingProduct.setPrice(dto.getPrice());
            existingProduct.setDescription(dto.getDescription());
            existingProduct.setThumbnail(dto.getThumbnail());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> existingProduct= productRepository.findById(id);
        existingProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Product product, ProductImageDTO dto){
        long productId = product.getId();
        ProductImage productImage = ProductImage.builder().product(product)
                .image(dto.getImage())
                .build();
        isValidSize(productId);
        return productImageRepository.save(productImage);
    }

    public void isValidSize(long productId){
        int productSize= productImageRepository.findAllByProductId(productId).size();
        if (productSize > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidParamException("Number of images must be <= "+ ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
    }
}
