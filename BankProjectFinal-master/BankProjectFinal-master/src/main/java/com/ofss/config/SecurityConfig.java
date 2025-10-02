package com.ofss.config;

import com.ofss.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(authz -> authz

                // Admin-only endpoints
                .requestMatchers("/users/**").hasRole("ADMIN")
                .requestMatchers("/banks/**").hasRole("ADMIN")
                .requestMatchers("/customers/**").hasAnyRole("ADMIN", "MANAGER")
                // Account endpoints
                .requestMatchers("/accounts").hasAnyRole("ADMIN", "MANAGER")
                .requestMatchers("/accounts/id/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                // Transaction endpoints - accessible by all authenticated users
                .requestMatchers("/accounts/deposit/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                .requestMatchers("/accounts/withdraw/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                .requestMatchers("/accounts/transfer/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                .requestMatchers("/transactions/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                // Any other request needs authentication
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
