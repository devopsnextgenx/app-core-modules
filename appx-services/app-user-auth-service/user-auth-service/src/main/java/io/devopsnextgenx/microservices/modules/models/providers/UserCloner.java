package io.devopsnextgenx.microservices.modules.models.providers;

import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;

import io.devopsnextgenx.microservices.modules.models.User;
import io.devopsnextgenx.microservices.modules.userauth.user.dto.UserDto;

public class UserCloner extends AbstractCloner<User, UserDto> {
    @Override
    public void updateTypeMap() {
        TypeMap<User, UserDto> propMap = getModelMapper().createTypeMap(User.class, UserDto.class);
        propMap.addMappings(mapping -> {
            mapping.skip(UserDto::setPassword);
        });
        // PropertyMap<User, UserDto> skipModifiedFieldsMap = new PropertyMap<User, UserDto>() {
        //     protected void configure() {
        //         skip().setPassword(null);;
        //     }
        // };
        // propMap.addMappings(skipModifiedFieldsMap);
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
