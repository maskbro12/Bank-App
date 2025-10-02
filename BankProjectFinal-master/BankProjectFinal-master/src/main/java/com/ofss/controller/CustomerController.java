package com.ofss.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.ofss.model.Bank;
import com.ofss.model.Customer;
import com.ofss.services.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/id/{customerId}")
    public ResponseEntity<Object> getCustomerById(@PathVariable("customerId") int customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<Object> addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/id/{customerId}")
    public ResponseEntity<Object> updateCustomer(@PathVariable("customerId") int customerId, @RequestBody Customer customer) {
        return customerService.updateCustomer(customerId, customer);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PatchMapping("/id/{customerId}")
    public ResponseEntity<Object> patchCustomer(@PathVariable("customerId") int customerId, @RequestBody Customer customer) {
        return customerService.patchCustomer(customerId, customer);
    }
}