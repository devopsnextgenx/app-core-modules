package io.devopsnextgenx.microservices.modules.models.providers;

public interface CloneCopyStrategy<T,P> {
    P cloneToDto(T cloneMe);
    T cloneToModel(P cloneMe);
    T copyToModel(P from, T to);
}