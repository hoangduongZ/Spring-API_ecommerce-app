package com.duonghoang.shopapp_backend.services.product;

import com.duonghoang.shopapp_backend.dtos.ProductDTO;
import com.duonghoang.shopapp_backend.dtos.ProductImageDTO;
import com.duonghoang.shopapp_backend.exception.DataNotFoundException;
import com.duonghoang.shopapp_backend.models.Product;
import com.duonghoang.shopapp_backend.models.ProductImage;
import com.duonghoang.shopapp_backend.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;

    Product getProductById(Long id) throws Exception;

    Page<ProductResponse> getAllProducts(String keyword,
                                         Long categoryId, PageRequest pageRequest);

    Product updateProduct(long id, ProductDTO dto);

    void deleteProduct(long id);

    boolean existByName(String name);

    ProductImage createProductImage(Product product, ProductImageDTO dto);

    void isValidSize(long productId);

    List<Product> findProductByIds(List<Long> ids);
}
