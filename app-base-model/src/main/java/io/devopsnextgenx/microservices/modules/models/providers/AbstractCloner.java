package io.devopsnextgenx.microservices.modules.models.providers;

import lombok.Data;

import org.modelmapper.ModelMapper;

@Data
public abstract class AbstractCloner<T, P> implements CloneCopyStrategy<T,P> {
    private ModelMapper modelMapper;

    public AbstractCloner() {
        setModelMapper(new ModelMapper());
        updateTypeMap();
    }

    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ModelMapper getModelMapper(){
        return this.modelMapper;
    }

    public abstract void updateTypeMap();
}