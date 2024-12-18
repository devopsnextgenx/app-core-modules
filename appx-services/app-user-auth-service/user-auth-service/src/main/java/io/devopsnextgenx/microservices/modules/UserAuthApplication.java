package io.devopsnextgenx.microservices.modules;

import io.devopsnextgenx.microservices.modules.access.model.AuthenticationFacade;
import io.devopsnextgenx.microservices.modules.auth.service.AuthService;
import io.devopsnextgenx.microservices.modules.loader.configuration.DataLoaderProperties;
import io.devopsnextgenx.microservices.modules.loader.services.ImportXmlService;
import io.devopsnextgenx.microservices.modules.utils.creators.Creator;
import io.devopsnextgenx.microservices.modules.org.creators.OrgCreator;
import io.devopsnextgenx.microservices.modules.org.importxml.OrgPostXmlImportServiceProcessor;
import io.devopsnextgenx.microservices.modules.org.models.Organization;
import io.devopsnextgenx.microservices.modules.org.repository.OrgRepository;
import io.devopsnextgenx.microservices.modules.repository.IdMapperRepository;
import io.devopsnextgenx.microservices.modules.security.configuration.AuthServiceProperties;
import io.devopsnextgenx.microservices.modules.user.creators.RoleCreator;
import io.devopsnextgenx.microservices.modules.user.creators.UserCreator;
import io.devopsnextgenx.microservices.modules.user.models.Role;
import io.devopsnextgenx.microservices.modules.user.models.User;
import io.devopsnextgenx.microservices.modules.user.models.UserCloner;
import io.devopsnextgenx.microservices.modules.user.repository.RoleRepository;
import io.devopsnextgenx.microservices.modules.user.repository.UserRepository;
import io.devopsnextgenx.microservices.modules.user.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@ComponentScan("io.devopsnextgenx.microservices.modules")
@EnableJpaRepositories("io.devopsnextgenx.microservices.modules")
public class UserAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserAuthApplication.class, args);
    }

    // TODO move this to security/authentication module, as its more relevent
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AuthenticationFacade authenticationFacade() {
        return new AuthenticationFacade();
    }
    
    @Bean
    @ConditionalOnMissingBean(name = "restTemplate")
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public UserCreator userCreator(IdMapperRepository idMapperRepository, UserRepository userRepository) {
        return new UserCreator(idMapperRepository, userRepository);
    }

    @Bean
    public OrgCreator orgCreator(IdMapperRepository idMapperRepository, OrgRepository orgRepository) {
        return new OrgCreator(idMapperRepository, orgRepository);
    }

    @Bean
    public RoleCreator roleCreator(IdMapperRepository idMapperRepository, RoleRepository roleRepository) {
        return new RoleCreator(idMapperRepository, roleRepository);
    }

    @Bean
    public Map<String, Creator> creatorMap(UserCreator userCreator, RoleCreator roleCreator, OrgCreator orgCreator) {
        Map<String, Creator> creatorMap = new HashMap<>();
        creatorMap.put(User.class.getSimpleName(), userCreator);
        creatorMap.put(Role.class.getSimpleName(), roleCreator);
        creatorMap.put(Organization.class.getSimpleName(), orgCreator);
        return creatorMap;
    }

    @Bean
    public ImportXmlService importXmlService(AuthenticationFacade authenticationFacade, DataLoaderProperties dataLoaderProperties, IdMapperRepository idMapperRepository, UserCreator userCreator, RoleCreator roleCreator, OrgCreator orgCreator) {
        return new ImportXmlService(creatorMap(userCreator, roleCreator, orgCreator),
            dataLoaderProperties, idMapperRepository, authenticationFacade,
                Arrays.asList(OrgPostXmlImportServiceProcessor.builder().build()));
    }

    @Bean
    public AuthService authService(RestTemplate restTemplate, AuthServiceProperties authServiceProperties) {
        return new AuthService(restTemplate, authServiceProperties);
    }

    @Bean
    public UserCloner userCloner() {
        return new UserCloner();
    }

    @Bean
    public UserService userService(UserRepository userRepository, UserCloner userCloner) {
        return new UserService(userRepository, userCloner);
    }
}
