package io.devopsnextgenx.microservices.modules.models.providers;

import lombok.Data;

import org.modelmapper.ModelMapper;

@Data
public abstract class AbstractCloner<T, P> implements CloneCopyStrategy<T,P> {
    private ModelMapper modelMapper;

    protected AbstractCloner() {
        setModelMapper(new ModelMapper());
        updateTypeMap();
    }

    public abstract void updateTypeMap();
}