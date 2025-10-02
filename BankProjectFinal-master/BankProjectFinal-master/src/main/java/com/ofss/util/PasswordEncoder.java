package com.ofss.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Generate encoded passwords for testing
        System.out.println("admin123: " + encoder.encode("admin123"));
        System.out.println("manager123: " + encoder.encode("manager123"));
        System.out.println("user123: " + encoder.encode("user123"));
        System.out.println("password123: " + encoder.encode("password123"));
    }
}
