
package com.example.bookmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.bookmanagement.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

            // Disable CSRF for development
            .csrf(csrf -> csrf.disable())

            // Authorization Rules
            .authorizeHttpRequests(auth -> auth

                // Public Pages
                .requestMatchers(
                        "/login",
                        "/register",
                        "/forgot-password",
                        "/css/**",
                        "/js/**",
                        "/images/**")
                .permitAll()

                // Admin Pages
                .requestMatchers("/admin/**")
                .hasRole("ADMIN")

                // Authenticated Users
                .requestMatchers(
                        "/dashboard",
                        "/books/**",
                        "/change-password")
                .hasAnyRole("USER", "ADMIN")

                // Any Other Request
                .anyRequest()
                .authenticated()
            )

            // Login Configuration
            .formLogin(form -> form

                    .loginPage("/login")

                    .defaultSuccessUrl(
                            "/dashboard",
                            true)

                    .permitAll()
            )

            // Logout Configuration
            .logout(logout -> logout

                    .logoutUrl("/logout")

                    .logoutSuccessUrl(
                            "/login?logout")

                    .invalidateHttpSession(true)

                    .deleteCookies(
                            "JSESSIONID")

                    .permitAll()
            )

            // Authentication Provider
            .authenticationProvider(
                    authenticationProvider());

        return http.build();
    }
}

