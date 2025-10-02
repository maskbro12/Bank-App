package com.ofss.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.ofss.exception.InsufficientFundsException;
import com.ofss.model.Account;
import com.ofss.model.AccountRepository;
import com.ofss.model.Bank;
import com.ofss.model.BankRepository;
import com.ofss.model.Customer;
import com.ofss.model.CustomerRepository;
import com.ofss.repository.TransactionRepository;
import com.ofss.model.Transaction;
import java.time.LocalDate;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BankRepository bankRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public ResponseEntity<Object> addAccount(Account account){
        // Ensure accountNumber is zero so JPA treats this as a new entity
        account.setAccountNumber(0);
        // Fetch Bank and Customer from DB using IDs
        Bank bank = null;
        Customer cust = null;
        if (account.getBank() != null && account.getBank().getBankId() > 0) {
            Optional<Bank> bankOpt = bankRepository.findById(account.getBank().getBankId());
            if (bankOpt.isPresent()) {
                bank = bankOpt.get();
            } else {
                return ResponseEntity.badRequest().body("Bank with id " + account.getBank().getBankId() + " not found");
            }
        }
        if (account.getCust() != null && account.getCust().getCustId() > 0) {
            Optional<Customer> custOpt = customerRepository.findById(account.getCust().getCustId());
            if (custOpt.isPresent()) {
                cust = custOpt.get();
            } else {
                return ResponseEntity.badRequest().body("Customer with id " + account.getCust().getCustId() + " not found");
            }
        }
        account.setBank(bank);
        account.setCust(cust);
		// Saves a given entity. Use the returned instance for further 
		// operations as the save operation might have changed theentity instance completely.
		// save() method will generate required INSERT query
		accountRepository.save(account);
		return ResponseEntity.ok("Account added successfully!");
	}
	
    public ArrayList<Account> getAllAccounts() {
		ArrayList<Account> allAccounts=new ArrayList<>();
		accountRepository.findAll().forEach(account -> allAccounts.add(account));
		return allAccounts;
	}

    public ResponseEntity<Object> depositMoney(int accountId, BigDecimal amount) {
        Optional<Account> optional = accountRepository.findById(accountId);
        if (optional.isPresent()) {
            Account account = optional.get();
            account.setBalance(account.getBalance().add(amount));
            accountRepository.save(account);
            // Log transaction
            Transaction transaction = new Transaction(
                (long) accountId, // destinationAccountId
                LocalDate.now(),
                "DEPOSIT",
                amount,
                account.getCust()
            );
            transactionRepository.save(transaction);
            return ResponseEntity.ok("Deposit successful. New balance: " + account.getBalance());
        }
        return ResponseEntity.badRequest().body("Account not found");
    }

    public ResponseEntity<Object> withdrawMoney(int accountId, BigDecimal amount) {
        Optional<Account> optional = accountRepository.findById(accountId);
        if (optional.isPresent()) {
            Account account = optional.get();
            if (account.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds for withdrawal");
            }
            account.setBalance(account.getBalance().subtract(amount));
            accountRepository.save(account);
            // Save withdrawal transaction
            Transaction transaction = new Transaction(
                (long) account.getAccountNumber(),
                LocalDate.now(),
                "WITHDRAW",
                amount,
                account.getCust()
            );
            transactionRepository.save(transaction);
            return ResponseEntity.ok("Withdrawal successful. New balance: " + account.getBalance());
        }
        return ResponseEntity.badRequest().body("Account not found");
    }

    public ResponseEntity<Object> transferMoney(int fromAccountId, int toAccountId, BigDecimal amount) {
        Optional<Account> fromOpt = accountRepository.findById(fromAccountId);
        Optional<Account> toOpt = accountRepository.findById(toAccountId);
        if (fromOpt.isPresent() && toOpt.isPresent()) {
            Account from = fromOpt.get();
            Account to = toOpt.get();
            if (from.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds for transfer");
            }
            from.setBalance(from.getBalance().subtract(amount));
            to.setBalance(to.getBalance().add(amount));
            accountRepository.save(from);
            accountRepository.save(to);
            // Save transfer transactions
            Transaction senderTransaction = new Transaction(
                (long) to.getAccountNumber(),
                LocalDate.now(),
                "TRANSFER_OUT",
                amount,
                from.getCust()
            );
            transactionRepository.save(senderTransaction);
            Transaction receiverTransaction = new Transaction(
                (long) to.getAccountNumber(),
                LocalDate.now(),
                "TRANSFER_IN",
                amount,
                to.getCust()
            );
            transactionRepository.save(receiverTransaction);
            return ResponseEntity.ok("Transfer successful. Sender balance: " + from.getBalance() + ", Receiver balance: " + to.getBalance());
        }
        return ResponseEntity.badRequest().body("One or both accounts not found");
    }

    public ResponseEntity<Object> viewBalance(int accountId) {
        Optional<Account> optional = accountRepository.findById(accountId);
        if (optional.isPresent()) {
            Account account = optional.get();
            return ResponseEntity.ok("Current balance: " + account.getBalance());
        }
        return ResponseEntity.badRequest().body("Account not found");
    }

    public ResponseEntity<Object> getAccountById(int accountId) {
        Optional<Account> optional = accountRepository.findById(accountId);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        }
        return ResponseEntity.badRequest().body("Account not found");
    }

    public ResponseEntity<Object> updateAccount(int accountId, Account account) {
        Optional<Account> optional = accountRepository.findById(accountId);
        if (optional.isPresent()) {
            Account existing = optional.get();
            existing.setAccountCreationDate(account.getAccountCreationDate());
            existing.setAccountType(account.getAccountType());
            existing.setBalance(account.getBalance());
            existing.setIfscCode(account.getIfscCode());
            existing.setCust(account.getCust());
            existing.setBank(account.getBank());
            accountRepository.save(existing);
            return ResponseEntity.ok("Account updated successfully!");
        }
        return ResponseEntity.badRequest().body("Account not found");
    }

    public ResponseEntity<Object> patchAccount(int accountId, Account account) {
        Optional<Account> optional = accountRepository.findById(accountId);
        if (optional.isPresent()) {
            Account existing = optional.get();
            if (account.getAccountCreationDate() != null) existing.setAccountCreationDate(account.getAccountCreationDate());
            if (account.getAccountType() != null) existing.setAccountType(account.getAccountType());
            if (account.getBalance() != null) existing.setBalance(account.getBalance());
            if (account.getIfscCode() != null) existing.setIfscCode(account.getIfscCode());
            if (account.getCust() != null) existing.setCust(account.getCust());
            if (account.getBank() != null) existing.setBank(account.getBank());
            accountRepository.save(existing);
            return ResponseEntity.ok("Account patched successfully!");
        }
        return ResponseEntity.badRequest().body("Account not found");
    }
}