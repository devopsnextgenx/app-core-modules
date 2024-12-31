package io.devopsnextgenx.microservices.modules.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.devopsnextgenx.microservices.modules.product.collections.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

    Product findByName(String name);

}