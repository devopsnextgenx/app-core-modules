package io.devopsnextgenx.microservices.modules.product.mappers;

import org.modelmapper.TypeMap;

import io.devopsnextgenx.microservices.modules.dto.ProductDto;
import io.devopsnextgenx.microservices.modules.models.providers.AbstractCloner;
import io.devopsnextgenx.microservices.modules.product.collections.Product;

public class ProductMapper extends AbstractCloner<Product, ProductDto> {
    @Override
    public void updateTypeMap() {
        TypeMap<ProductDto, Product> productTypeMap = getModelMapper().createTypeMap(ProductDto.class, Product.class);
    }

    @Override
    public ProductDto cloneToDto(Product cloneMe) {
        return getModelMapper().map(cloneMe, ProductDto.class);
    }

    @Override
    public Product cloneToModel(ProductDto cloneMe) {
        return getModelMapper().map(cloneMe, Product.class);
    }

    @Override
    public Product copyToModel(ProductDto from, Product to) {
        getModelMapper().map(from, to);
        return to;
    }

}
