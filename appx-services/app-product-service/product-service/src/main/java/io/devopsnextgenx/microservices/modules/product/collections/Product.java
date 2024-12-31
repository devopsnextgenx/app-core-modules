package io.devopsnextgenx.microservices.modules.product.collections;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private double price;
    private String imgLogo;
    private List<String> imgs;
    private List<String> tags;
    private boolean deleted;
    private String created;
    private String updated;

}
