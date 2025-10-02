package com.ofss.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ofss.model.Bank;
import com.ofss.model.BankRepository;

@Service
public class BankServices {

	@Autowired
	private BankRepository bankRepository;
	
	public ResponseEntity<Object> addBank(Bank bank){
		// Saves a given entity. Use the returned instance for further 
		// operations as the save operation might have changed theentity instance completely.
		// save() method will generate required INSERT query
		bankRepository.save(bank);
		return ResponseEntity.ok("Bank added successfully!");
	}
	
	public ArrayList<Bank> getAllBanks() {
		ArrayList<Bank> allBanks=new ArrayList<>();
		bankRepository.findAll().forEach(bank -> allBanks.add(bank));
		return allBanks;
	}
	
	public ResponseEntity<Object> deleteABankById(int bankId) {
		System.out.println("bankId passed is "+bankId);
		Optional<Bank> optional=bankRepository.findById(bankId);
		if(optional.isPresent()) {
            bankRepository.deleteById(bankId);
            return ResponseEntity.ok("Bank deleted successfully!");
		
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank with id "+bankId+" not found");
	}

    public ResponseEntity<Object> getBankById(int bankId) {
        Optional<Bank> optional = bankRepository.findById(bankId);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank with id " + bankId + " not found");
    }

    public ResponseEntity<Object> updateBank(int bankId, Bank bank) {
        Optional<Bank> optional = bankRepository.findById(bankId);
        if (optional.isPresent()) {
            Bank existing = optional.get();
            existing.setBankName(bank.getBankName());
            existing.setBranchName(bank.getBranchName());
            existing.setIfscCode(bank.getIfscCode());
            bankRepository.save(existing);
            return ResponseEntity.ok("Bank updated successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank with id " + bankId + " not found");
    }

    public ResponseEntity<Object> patchBank(int bankId, Bank bank) {
        Optional<Bank> optional = bankRepository.findById(bankId);
        if (optional.isPresent()) {
            Bank existing = optional.get();
            if (bank.getBankName() != null) existing.setBankName(bank.getBankName());
            if (bank.getBranchName() != null) existing.setBranchName(bank.getBranchName());
            if (bank.getIfscCode() != null) existing.setIfscCode(bank.getIfscCode());
            bankRepository.save(existing);
            return ResponseEntity.ok("Bank patched successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Bank with id " + bankId + " not found");
    }

}
