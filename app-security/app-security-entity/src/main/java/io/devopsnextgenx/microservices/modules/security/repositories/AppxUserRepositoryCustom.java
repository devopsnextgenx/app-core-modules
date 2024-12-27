package io.devopsnextgenx.microservices.modules.security.repositories;

public interface AppxUserRepositoryCustom<T, S> {
        public T findByUsername(S username);
}
