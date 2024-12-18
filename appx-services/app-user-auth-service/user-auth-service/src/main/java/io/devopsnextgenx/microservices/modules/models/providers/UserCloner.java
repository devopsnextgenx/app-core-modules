package io.devopsnextgenx.microservices.modules.models.providers;

import io.devopsnextgenx.microservices.modules.models.User;
import io.devopsnextgenx.microservices.modules.userauth.user.dto.UserDto;

public class UserCloner extends AbstractCloner<User, UserDto> {
    @Override
    public void updateTypeMap() {
    }

    @Override
    public UserDto cloneToDto(User user) {
        return getModelMapper().map(user, UserDto.class);
    }

    @Override
    public User cloneToModel(UserDto user) {
        return getModelMapper().map(user, User.class);
    }

    @Override
    public User copyToModel(UserDto from, User to) {
        getModelMapper().map(from, to);
        return to;
    }

}
