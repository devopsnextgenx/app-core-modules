package io.devopsnextgenx.microservices.modules.models.providers;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.TypeMap;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import io.devopsnextgenx.microservices.modules.access.model.ROLE;
import io.devopsnextgenx.microservices.modules.security.models.Role;
import io.devopsnextgenx.microservices.modules.security.models.User;
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
        
        TypeMap<UserDto, User> userDtoTypeMap = getModelMapper().createTypeMap(UserDto.class, User.class);

        Converter<List<RoleDto>, List<Role>> roleDtoListConverter = new Converter<List<RoleDto>, List<Role>>() {
            @Override
            public List<Role> convert(MappingContext<List<RoleDto>, List<Role>> context) {
            List<RoleDto> source = context.getSource();
            List<Role> destination = new ArrayList<>();
            if (source != null) {
                for (RoleDto role : source) {
                    destination.add(Role.builder().name(ROLE.valueOf(role.getValue())).build());
                }
            }
            return destination;
            }
        };
        userDtoTypeMap.addMappings(mapping -> mapping.using(roleDtoListConverter).map(UserDto::getRoles, User::setUserRoles));
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
