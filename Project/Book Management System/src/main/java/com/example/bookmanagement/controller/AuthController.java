
package com.example.bookmanagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.bookmanagement.dto.ChangePasswordDTO;
import com.example.bookmanagement.dto.ForgotPasswordDTO;
import com.example.bookmanagement.dto.RegisterRequest;
import com.example.bookmanagement.model.Role;
import com.example.bookmanagement.model.User;
import com.example.bookmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // Login Page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Registration Page
    @GetMapping("/register")
    public String registerForm(Model model) {

        model.addAttribute(
                "registerRequest",
                new RegisterRequest());

        return "register";
    }

    // Register User
    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute RegisterRequest request,
            Model model) {

        if (userRepository.existsByUsername(
                request.getUsername())) {

            model.addAttribute(
                    "error",
                    "Username already exists");

            return "register";
        }

        if (userRepository.existsByEmail(
                request.getEmail())) {

            model.addAttribute(
                    "error",
                    "Email already registered");

            return "register";
        }

        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(
                        request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);

        return "redirect:/login?registered";
    }

    // Change Password Page (Logged-in User)
    @GetMapping("/change-password")
    public String changePasswordForm(Model model) {

        model.addAttribute(
                "passwordDTO",
                new ChangePasswordDTO());

        return "change-password";
    }

    // Change Password Logic
    @PostMapping("/change-password")
    public String changePassword(
            @ModelAttribute("passwordDTO")
            ChangePasswordDTO dto,
            Model model) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username =
                authentication.getName();

        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));

        // Verify current password
        if (!passwordEncoder.matches(
                dto.getCurrentPassword(),
                user.getPassword())) {

            model.addAttribute(
                    "error",
                    "Current password is incorrect");

            return "change-password";
        }

        // Verify new passwords match
        if (!dto.getNewPassword()
                .equals(dto.getConfirmPassword())) {

            model.addAttribute(
                    "error",
                    "New passwords do not match");

            return "change-password";
        }

        // Update password
        user.setPassword(
                passwordEncoder.encode(
                        dto.getNewPassword()));

        userRepository.save(user);

        model.addAttribute(
                "success",
                "Password changed successfully");

        model.addAttribute(
                "passwordDTO",
                new ChangePasswordDTO());

        return "change-password";
    }

    // Forgot Password Page
    @GetMapping("/forgot-password")
    public String forgotPasswordPage(Model model) {

        model.addAttribute(
                "forgotPasswordDTO",
                new ForgotPasswordDTO());

        return "forgot-password";
    }

    
// Forgot Password Logic
@PostMapping("/forgot-password")
public String forgotPassword(
        @ModelAttribute("forgotPasswordDTO")
        ForgotPasswordDTO dto,
        Model model) {

    User user = userRepository
            .findByUsername(dto.getUsername())
            .orElse(null);

    if (user == null) {

        model.addAttribute(
                "error",
                "User not found");

        return "forgot-password";
    }

    if (!dto.getNewPassword()
            .equals(dto.getConfirmPassword())) {

        model.addAttribute(
                "error",
                "Passwords do not match");

        return "forgot-password";
    }

    user.setPassword(
            passwordEncoder.encode(
                    dto.getNewPassword()));

    userRepository.save(user);

    return "redirect:/login?resetSuccess";
}
}

