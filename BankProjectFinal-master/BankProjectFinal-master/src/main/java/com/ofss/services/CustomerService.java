package com.ofss.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ofss.model.Customer;
import com.ofss.model.CustomerRepository;
import com.ofss.model.User;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseEntity<Object> addCustomer(Customer cust) {
        try {
            // Save the customer first
            Customer savedCustomer = customerRepository.save(cust);

            // Generate username from customer email (before @ symbol) or use firstName + custId
            String username = cust.getEmailId() != null && cust.getEmailId().contains("@")
                ? cust.getEmailId().substring(0, cust.getEmailId().indexOf("@"))
                : cust.getFirstName().toLowerCase() + savedCustomer.getCustId();

            // Check if username already exists, if so append customer ID
            if (userService.existsByUsername(username)) {
                username = username + savedCustomer.getCustId();
            }

            // Create corresponding user account with default password "user123"
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword("user123"); // Will be encoded in UserService.createUser()
            newUser.setUserType("USER"); // Default user type
            newUser.setFirstName(savedCustomer.getFirstName());
            newUser.setLastName(savedCustomer.getLastName());
            newUser.setEmail(savedCustomer.getEmailId());
            newUser.setEnabled(true);

            // Create the user account
            userService.createUser(newUser);

            return ResponseEntity.ok("Customer and user account created successfully! Username: " + username);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating customer and user account: " + e.getMessage());
        }
    }

    public ArrayList<Customer> getAllCustomers() {
		ArrayList<Customer> allCustomers=new ArrayList<>();
		customerRepository.findAll().forEach(customer -> allCustomers.add(customer));
		return allCustomers;
	}

    public ResponseEntity<Object> getCustomerById(int customerId) {
        Optional<Customer> optional = customerRepository.findById(customerId);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with id " + customerId + " not found");
    }

    public ResponseEntity<Object> updateCustomer(int customerId, Customer customer) {
        Optional<Customer> optional = customerRepository.findById(customerId);
        if (optional.isPresent()) {
            Customer existing = optional.get();
            existing.setFirstName(customer.getFirstName());
            existing.setLastName(customer.getLastName());
            existing.setPhoneNumber(customer.getPhoneNumber());
            existing.setEmailId(customer.getEmailId());
            customerRepository.save(existing);
            return ResponseEntity.ok("Customer updated successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with id " + customerId + " not found");
    }

    public ResponseEntity<Object> patchCustomer(int customerId, Customer customer) {
        Optional<Customer> optional = customerRepository.findById(customerId);
        if (optional.isPresent()) {
            Customer existing = optional.get();
            if (customer.getFirstName() != null) existing.setFirstName(customer.getFirstName());
            if (customer.getLastName() != null) existing.setLastName(customer.getLastName());
            if (customer.getPhoneNumber() != null) existing.setPhoneNumber(customer.getPhoneNumber());
            if (customer.getEmailId() != null) existing.setEmailId(customer.getEmailId());
            customerRepository.save(existing);
            return ResponseEntity.ok("Customer patched successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer with id " + customerId + " not found");
    }
}