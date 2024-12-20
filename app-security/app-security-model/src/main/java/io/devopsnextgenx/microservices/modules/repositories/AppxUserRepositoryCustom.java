package io.devopsnextgenx.microservices.modules.repositories;

public interface AppxUserRepositoryCustom<T, S> {
        public T findByUsername(S username);
}
