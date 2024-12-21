package io.devopsnextgenx.microservices.modules.models.providers;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.TypeMap;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import io.devopsnextgenx.microservices.modules.models.Role;
import io.devopsnextgenx.microservices.modules.models.User;
import io.devopsnextgenx.microservices.modules.userauth.user.dto.RoleDto;
import io.devopsnextgenx.microservices.modules.userauth.user.dto.UserDto;

public class UserCloner extends AbstractCloner<User, UserDto> {
    @Override
    public void updateTypeMap() {
        TypeMap<User, UserDto> userTypeMap = getModelMapper().createTypeMap(User.class, UserDto.class);
        userTypeMap.addMappings(mapping -> {
            mapping.skip(UserDto::setPassword);
        });
        Converter<List<Role>, List<RoleDto>> roleListConverter = new Converter<List<Role>, List<RoleDto>>() {
            @Override
            public List<RoleDto> convert(MappingContext<List<Role>, List<RoleDto>> context) {
            List<Role> source = context.getSource();
            List<RoleDto> destination = new ArrayList<>();
            if (source != null) {
                for (Role role : source) {
                    destination.add(RoleDto.valueOf(role.getName().name()));
                }
            }
            return destination;
            }
        };
        userTypeMap.addMappings(mapping -> mapping.using(roleListConverter).map(User::getUserRoles, UserDto::setRoles));
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
