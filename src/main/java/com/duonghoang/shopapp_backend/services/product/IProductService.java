package com.duonghoang.shopapp_backend.services.product;

import com.duonghoang.shopapp_backend.dtos.ProductDTO;
import com.duonghoang.shopapp_backend.dtos.ProductImageDTO;
import com.duonghoang.shopapp_backend.exception.DataNotFoundException;
import com.duonghoang.shopapp_backend.models.Product;
import com.duonghoang.shopapp_backend.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    Product getProductById(Long id) throws Exception;

    Page<Product> getAllProducts(PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO dto);

    void deleteProduct(long id);

    boolean existByName(String name);

    ProductImage createProductImage(Product product, ProductImageDTO dto);

    void isValidSize(long productId);
}
