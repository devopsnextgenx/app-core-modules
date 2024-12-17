package io.devopsnextgenx.microservices.modules.user.models;

import io.devopsnextgenx.microservices.modules.models.providers.AbstractCloner;

public class UserCloner extends AbstractCloner<User, User> {
    @Override
    public void updateTypeMap() {
    }

    @Override
    public User cloneToDto(User user) {
        return getModelMapper().map(user, User.class);
    }

    @Override
    public User cloneToModel(User user) {
        return getModelMapper().map(user, User.class);
    }

    @Override
    public User copyToModel(User from, User to) {
        getModelMapper().map(from, to);
        return to;
    }

}
