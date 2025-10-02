package com.ofss.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ofss.model.Bank;
import com.ofss.services.BankServices;

@RestController
@RequestMapping("/banks")
public class BankController {

    @Autowired
    private BankServices bankService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Object> addBank(@RequestBody Bank bank) {
        return bankService.addBank(bank);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping
    public List<Bank> getAllBanks() {
        return bankService.getAllBanks();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/id/{bankId}")
    public ResponseEntity<Object> getBankById(@PathVariable("bankId") int bankId) {
        return bankService.getBankById(bankId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/id/{bankId}")
    public ResponseEntity<Object> updateBank(@PathVariable("bankId") int bankId, @RequestBody Bank bank) {
        return bankService.updateBank(bankId, bank);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/id/{bankId}")
    public ResponseEntity<Object> patchBank(@PathVariable("bankId") int bankId, @RequestBody Bank bank) {
        return bankService.patchBank(bankId, bank);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/id/{bankId}")
    public ResponseEntity<Object> removeAAccount(@PathVariable("bankId") int bankId) {
        return bankService.deleteABankById(bankId);
    }
}