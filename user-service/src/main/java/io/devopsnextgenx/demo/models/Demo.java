package io.devopsnextgenx.demo.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "demo")
@Data
public class Demo {
    @Id
    private Long id;
    private String name;
    private String description;
    private Double price;

}
