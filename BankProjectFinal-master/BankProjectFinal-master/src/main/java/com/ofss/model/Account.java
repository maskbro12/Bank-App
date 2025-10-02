package com.ofss.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ACCOUNT")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountNumber;
    private LocalDate accountCreationDate;
    private String accountType;
    private BigDecimal balance;
    private String ifscCode;

    @ManyToOne
    @JoinColumn(name = "custId", nullable = false)
    private Customer cust;

    @ManyToOne
    @JoinColumn(name = "bankId", nullable = false)
    private Bank bank;

    public Account() {}

    public Account(LocalDate accountCreationDate, String accountType, BigDecimal balance, String ifscCode, Customer cust, Bank bank) {
        this.accountCreationDate = accountCreationDate;
        this.accountType = accountType;
        this.balance = balance;
        this.ifscCode = ifscCode;
        this.cust = cust;
        this.bank = bank;
    }

    public int getAccountNumber() { return accountNumber; }
    public void setAccountNumber(int accountNumber) { this.accountNumber = accountNumber; }

    public LocalDate getAccountCreationDate() { return accountCreationDate; }
    public void setAccountCreationDate(LocalDate accountCreationDate) { this.accountCreationDate = accountCreationDate; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getIfscCode() { return ifscCode; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public Customer getCust() { return cust; }
    public void setCust(Customer cust) { this.cust = cust; }

    public Bank getBank() { return bank; }
    public void setBank(Bank bank) { this.bank = bank; }
}
