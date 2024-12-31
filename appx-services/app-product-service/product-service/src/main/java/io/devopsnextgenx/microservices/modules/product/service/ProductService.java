package io.devopsnextgenx.microservices.modules.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import io.devopsnextgenx.microservices.modules.dto.ProductDto;
import io.devopsnextgenx.microservices.modules.product.collections.Product;
import io.devopsnextgenx.microservices.modules.product.mappers.ProductMapper;
import io.devopsnextgenx.microservices.modules.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductDto createProduct(ProductDto product) {
        log.info("Creating product: {}", product);
        return productMapper.cloneToDto(productRepository.insert(productMapper.cloneToModel(product)));
    }

    public ProductDto getProduct(String id) {
        log.info("Getting product by id: {}", id);
        return productMapper.cloneToDto(productRepository.findById(id).orElse(null));
    }

    public ProductDto updateProduct(ProductDto product) {
        log.info("Updating product: {}", product);
        return productMapper.cloneToDto(productRepository.save(productMapper.cloneToModel(product)));
    }

    public void deleteProduct(String id) {
        log.info("Deleting product by id: {}", id);
        productRepository.deleteById(id);
    }

    public ProductDto getProductByName(String name) {
        log.info("Getting product by name: {}", name);
        return productMapper.cloneToDto(productRepository.findByName(name));
    }

    public List<ProductDto> listProducts() {
        log.info("Listing products");
        return productRepository.findAll().stream().map(product-> productMapper.cloneToDto(product)).toList();
    }
}
