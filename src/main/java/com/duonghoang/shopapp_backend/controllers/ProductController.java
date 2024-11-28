package com.duonghoang.shopapp_backend.controllers;

import com.duonghoang.shopapp_backend.dtos.ProductDTO;
import com.duonghoang.shopapp_backend.dtos.ProductImageDTO;
import com.duonghoang.shopapp_backend.models.Product;
import com.duonghoang.shopapp_backend.models.ProductImage;
import com.duonghoang.shopapp_backend.responses.ProductListResponse;
import com.duonghoang.shopapp_backend.responses.ProductResponse;
import com.duonghoang.shopapp_backend.services.product.IProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0", name = "category_id") Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
                Sort.by("id").ascending());
        Page<ProductResponse> productPages = productService.getAllProducts(keyword, categoryId,pageRequest);
        int totalPage = productPages.getTotalPages();
        List<ProductResponse> products = productPages.getContent();
        return ResponseEntity.status(200).body(
                ProductListResponse.builder().
                        products(products).
                        totalPages(totalPage).
                        build());
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long id) {
        try {
            Product existingProduct = productService.getProductById(id);
            return ResponseEntity.status(200).body(ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTO productDTO,
                                           BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                List<String> errorMessage = bindingResult.getFieldErrors()
                        .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product product = productService.createProduct(productDTO);
            return ResponseEntity.status(201).body(product);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{productId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@PathVariable("productId") long productId,
                                        @ModelAttribute ArrayList<MultipartFile> files) {
        try {
            Product existingProduct = productService.getProductById(productId);
            List<ProductImage> productImages = new ArrayList<>();
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("You can only upload maximum 5 image");
            }
            for (var file : files) {
                if (file.getSize() == 0) {
                    continue;
                }
                if (file.getSize() > 10 * 1024 * 1024) {
                    throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large");
                }

                if (isImageFile(file)) {
                    throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "File is not image");
                }

                productService.isValidSize(productId);
                String fileName = storeFile(file);
                ProductImage productImage = productService.createProductImage(existingProduct,
                        ProductImageDTO.builder().image(fileName).build());
                productImages.add(productImage);
            }
            return ResponseEntity.status(201).body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> viewImage(@PathVariable String imageName) {
        try {
            Path imagePath = Paths.get("uploads/" + imageName);
            UrlResource resource = new UrlResource(imagePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        if (isImageFile(file) || file.getOriginalFilename() == null) {
            throw new IOException("Invalid image format");
        }
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        Path uploadDir = Paths.get("uploads");

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }

    private boolean isImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType == null || !contentType.startsWith("image/");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") Long id) {
        return ResponseEntity.status(203).body("This is update product " + id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.status(200).body(String.format("Product with id %d deleted successfully !", id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> findProductsByIds(@RequestParam("ids") List<Long> ids){
        try{
            List<Product> product= productService.findProductByIds(ids);
            return ResponseEntity.ok().body(product);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //    @PostMapping("/generateFakeProducts")
    private ResponseEntity<String> generateFakeProduct() {
        Faker faker = new Faker();
        for (int i = 0; i < 500_000; i++) {
            String title = faker.commerce().productName();
            if (productService.existByName(title)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO
                    .builder()
                    .name(title)
                    .price((float) faker.number().numberBetween(10, 90_000_000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long) faker.number().numberBetween(1, 6))
                    .build();
            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok(null);
    }


}
