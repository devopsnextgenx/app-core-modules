package io.devopsnextgenx.microservices.modules.oauth2.utils;

import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;
import java.util.ArrayList;
import java.util.List;
import io.devopsnextgenx.microservices.modules.models.providers.AbstractCloner;
import io.devopsnextgenx.microservices.modules.security.models.User;
import io.devopsnextgenx.microservices.modules.security.models.Role;

public class UserCloner extends AbstractCloner<User, User> {
    @Override
    public void updateTypeMap() {
        TypeMap<User, User> userTypeMap = getModelMapper().createTypeMap(User.class, User.class);
        userTypeMap.addMappings(mapping -> {
            mapping.skip(User::setPassword);
        });
        Converter<List<Role>, List<Role>> roleListConverter = new Converter<List<Role>, List<Role>>() {
            @Override
            public List<Role> convert(MappingContext<List<Role>, List<Role>> context) {
                List<Role> source = context.getSource();
                List<Role> destination = new ArrayList<>();
                if (source != null) {
                    for (Role role : source) {
                        destination.add(getModelMapper().map(role, Role.class));
                    }
                }
                return destination;
            }
        };
        userTypeMap.addMappings(
                mapping -> mapping.using(roleListConverter).map(User::getUserRoles, User::setUserRoles));
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
