package com.ofss.controller;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.ofss.model.Account;
import com.ofss.model.DepositRequest;
import com.ofss.model.TransferRequest;
import com.ofss.model.WithdrawRequest;
import com.ofss.services.AccountService;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @GetMapping("/id/{accountId}")
    public ResponseEntity<Object> getAccountById(@PathVariable int accountId) {
        return accountService.getAccountById(accountId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping
    public ResponseEntity<Object> addAccount(@RequestBody Account account) {
        return accountService.addAccount(account);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/id/{accountId}")
    public ResponseEntity<Object> updateAccount(@PathVariable int accountId, @RequestBody Account account) {
        return accountService.updateAccount(accountId, account);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PatchMapping("/id/{accountId}")
    public ResponseEntity<Object> patchAccount(@PathVariable int accountId, @RequestBody Account account) {
        return accountService.patchAccount(accountId, account);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping("/deposit/accounts/id/{accountId}")
    public ResponseEntity<Object> depositMoney(@PathVariable int accountId, @RequestBody DepositRequest depositRequest) {
        return accountService.depositMoney(accountId, depositRequest.getDepositAmount());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping("/withdraw/accounts/id/{accountId}")
    public ResponseEntity<Object> withdrawMoney(@PathVariable int accountId, @RequestBody WithdrawRequest withdrawRequest) {
        return accountService.withdrawMoney(accountId, withdrawRequest.getWithdrawAmount());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping("/transfer/fromSourceAccount/toTargetAccount")
    public ResponseEntity<Object> transferMoney(@RequestBody TransferRequest transferRequest) {
        return accountService.transferMoney(transferRequest.getFromSourceAccount(), transferRequest.getToTargetAccount(), transferRequest.getTransferAmount());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping("/transfer/{fromSourceAccount}/{toTargetAccount}")
    public ResponseEntity<Object> transferMoney(
            @PathVariable int fromSourceAccount,
            @PathVariable int toTargetAccount,
            @RequestBody TransferRequest transferRequest) {
        return accountService.transferMoney(fromSourceAccount, toTargetAccount, transferRequest.getTransferAmount());
    }
}