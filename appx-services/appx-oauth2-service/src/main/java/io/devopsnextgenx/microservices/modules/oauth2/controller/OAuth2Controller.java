package io.devopsnextgenx.microservices.modules.oauth2.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.devopsnextgenx.microservices.modules.security.models.User;
import io.devopsnextgenx.microservices.modules.security.repositories.AppxUserRepository;
import io.devopsnextgenx.microservices.modules.oauth2.utils.UserCloner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/basic")
@SessionAttributes("user")
public class OAuth2Controller {

	private AppxUserRepository appxUserRepository;
	private PasswordEncoder passwordEncoder;
	private UserCloner userCloner;

	public OAuth2Controller(AppxUserRepository appxUserRepository, PasswordEncoder passwordEncoder, UserCloner userCloner) {
		this.appxUserRepository = appxUserRepository;
		this.passwordEncoder = passwordEncoder;
		this.userCloner = userCloner;
	}

    @ModelAttribute("user")
    public User setUpUserForm() {
        return new User(); // Initialize empty user for the session
    }

    @GetMapping("/register")
    public String showForm(Model model) {
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new User());
        }
        return "register";
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
    public String register(@ModelAttribute("user") User user, 
		SessionStatus sessionStatus,
		RedirectAttributes redirectAttributes) {
        // User userAccount = User.builder().firstName(firstName)
        // .lastName(lastName)
        // .userName(username)
		// .email(email)
        // .password(passwordEncoder.encode(password))
        // .active(true).build();

		appxUserRepository.save(user);
		try {
            // Process the user data here
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful!");
            return "redirect:/basic/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/basic/register";
        }
    }
	
    @GetMapping("/success")
    public String showSuccess(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("user", user);
        return "success";
    }
    
    @GetMapping("/clear")
    public String clearSession(SessionStatus sessionStatus) {
        sessionStatus.setComplete(); // Clear the session
        return "redirect:/basic/register";
    }
}
