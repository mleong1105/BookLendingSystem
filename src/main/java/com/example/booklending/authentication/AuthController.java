package com.example.booklending.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {
    @Autowired
    private AuthComponent authComponent;

    @GetMapping("/")
    public String home(Authentication authentication, HttpServletRequest request) {
        if (authentication == null || !authentication.isAuthenticated()) {
            String referrer = request.getHeader("referer");
            if (referrer == null) {
                return "redirect:/login";
            }
            else if (!referrer.contains("/login") && !referrer.contains("/signup")) {
                // If not authenticated and not coming from login, signup, or error pages, redirect to login
                return "redirect:/login";
            }
        }
        // If authenticated, show the home page
        return "home";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(name = "email") String email,
                        @RequestParam(name = "password") String password,
                        Model model) {
        boolean loginSuccess = authComponent.authenticateUserByEmailAndPassword(email, password);

        if (loginSuccess) {
            // Redirect to the home page or any other page on successful login
            return "redirect:/";
        } else {
            // If login fails, add an error attribute to display a message in the login page
            model.addAttribute("error", true);
            return "login";
        }
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam(name = "email") String email,
                         @RequestParam(name = "password") String password,
                         @RequestParam(name = "role") String role) {
        // Call Firebase API to create a new user with the provided email and password
        // Update Firebase Realtime Database with user information
        authComponent.createUser(email, password, role);
        return "redirect:/login";
    }
}
