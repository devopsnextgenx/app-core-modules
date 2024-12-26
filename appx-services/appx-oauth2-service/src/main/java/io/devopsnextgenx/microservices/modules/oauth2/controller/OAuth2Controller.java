package io.devopsnextgenx.microservices.modules.oauth2.controller;

import java.security.Principal;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.devopsnextgenx.microservices.modules.models.User;
import io.devopsnextgenx.microservices.modules.repositories.AppxUserRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * <b>Overview:</b>
 * <p>
 * 
 * 
 * <pre>
 * &#64;projectName oauth-service
 * Creation date: Nov 4, 2023
 * &#64;author Amit Kshirsagar
 * &#64;version 1.0
 * &#64;since
 * 
 * <p><b>Modification History:</b><p>
 * 
 * 
 * </pre>
 */

@Slf4j
@Controller
public class OAuth2Controller {

	private AppxUserRepository appxUserRepository;
	private PasswordEncoder passwordEncoder;

	public OAuth2Controller(AppxUserRepository appxUserRepository, PasswordEncoder passwordEncoder) {
		this.appxUserRepository = appxUserRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping(value = "/user")
	public ResponseEntity<Principal> user(Principal user) {
		return ResponseEntity.ok().body(user);
	}


	@GetMapping(value = "/error")
	public ResponseEntity<Void> error() {
		log.info("AS /error has been called");
		return ResponseEntity.noContent().build();
	}

    @PostMapping("/register")
    public ResponseEntity<User> register(
			@RequestParam("username") String username,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName) {
        User userAccount = User.builder().firstName(firstName)
        .lastName(lastName)
        .userName(username)
		.email(email)
        .password(passwordEncoder.encode(password))
        .active(true).build();
        return ResponseEntity.ok().body(appxUserRepository.save(userAccount));
    }
}
