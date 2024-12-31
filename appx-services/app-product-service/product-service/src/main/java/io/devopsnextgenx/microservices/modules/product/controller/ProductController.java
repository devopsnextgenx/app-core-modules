package io.devopsnextgenx.microservices.modules.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.devopsnextgenx.microservices.modules.api.ProductServiceApi;
import io.devopsnextgenx.microservices.modules.dto.ProductDto;
import io.devopsnextgenx.microservices.modules.product.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "JWT")
public class ProductController implements ProductServiceApi {
    private ProductService productService;

    @Override
    public ResponseEntity<List<ProductDto>> listProducts() {
        log.info("List products");
        return ResponseEntity.ok(productService.listProducts());
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(String productId) {
        log.info("Get product by id: {}", productId);
        return ResponseEntity.ok(new ProductDto());
    }

    @Override
    public ResponseEntity<ProductDto> postProduct(ProductDto productDto) {
        log.info("Create product: {}", productDto);
        return ResponseEntity.ok(productService.createProduct(productDto));
    }

    @Override
    public ResponseEntity<ProductDto> putProductById(String productId, ProductDto productDto) {
        log.info("Update product by id: {} with {}", productId, productDto);
        return ResponseEntity.ok(productService.updateProduct(productDto));
    }

    @Override
    public ResponseEntity<ProductDto> patchProductById(String productId, ProductDto productDto) {
        log.info("Patch product by id: {} with {}", productId, productDto);
        return ResponseEntity.ok(productDto);
    }

    @Override
    public ResponseEntity<Void> deleteProductById(String productId) {
        log.info("Delete product by id: {}", productId);
        return ResponseEntity.noContent().build();
    }
}
