package com.ofss.services;

import com.ofss.model.User;
import com.ofss.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Attempting to load user: " + username);

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            System.out.println("User found: " + user.getUsername() + ", Type: " + user.getUserType() + ", Enabled: " + user.isEnabled());

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(getAuthorities(user.getUserType()))
                    .accountExpired(false)
                    .accountLocked(false)
                    .credentialsExpired(false)
                    .disabled(!user.isEnabled())
                    .build();
        } catch (Exception e) {
            System.out.println("Error loading user " + username + ": " + e.getMessage());
            throw new UsernameNotFoundException("User not found: " + username, e);
        }
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String userType) {
        String role = "ROLE_" + userType.toUpperCase();
        System.out.println("Assigning role: " + role);
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
