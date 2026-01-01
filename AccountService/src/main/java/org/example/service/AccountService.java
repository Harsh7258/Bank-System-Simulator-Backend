package org.example.service;

import org.example.exception.AccountNotFound;
import org.example.exception.InsufficientBalanceException;
import org.example.exception.WrongInputException;
import org.example.model.AccStatus;
import org.example.model.Account;
import org.example.repo.AccountRepo;
import org.example.utils.AccountNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {
    @Autowired
    private AccountRepo accountRepository;

    public List<Account> findAll(){
        return  accountRepository.findAll();
    }

    public Account findByAccountNumber(String accountNo){
        Account account = accountRepository.findByAccountNo(accountNo);
        if(account == null){
            throw new AccountNotFound(accountNo);
        }
        return account;
    }

    public String createAccount(Account account){
        account.setHolderName(account.getHolderName());

        String accountNo;
        do {
            accountNo = AccountNumberGenerator.generate(account.getHolderName());
        } while (accountRepository.existsByAccountNo(accountNo));
        account.setAccountNo(accountNo);

        account.setAmount(0);
        account.setStatus(AccStatus.ACTIVE);
        account.setDateOfOpen(new Date());

        accountRepository.save(account);
        return "Account created successfully.";
    }

    public String changeStatus(String accountNo, String status) {
        Account account = accountRepository.findByAccountNo(accountNo);
        if(account == null){
            throw new AccountNotFound(accountNo);
        }

        if(status.equals("ACTIVE")){
            account.setStatus(AccStatus.ACTIVE);
            accountRepository.save(account);
        } else if(status.equals("INACTIVE")){
            account.setStatus(AccStatus.INACTIVE);
            accountRepository.save(account);
        } else {
            throw new WrongInputException("Please enter (ACTIVE) to Re-open or (INACTIVE) to close the account.");
        }

        return "Changed the account status to: " + status;
    }

    // Doubt in this service logic
    public Map<String, Object> updateBalance(String accountNo, double amount, String type, String targetAccountNo) {
        Account srcAcc = accountRepository.findByAccountNo(accountNo);
        if (srcAcc == null) {
            throw new AccountNotFound(accountNo);
        }

        switch (type) {
            case "CREDIT": srcAcc.setAmount(srcAcc.getAmount() + amount);
                break;
            case "DEBIT":
                if(srcAcc.getAmount() < amount){
                    throw new InsufficientBalanceException("Insufficient balance. Please try again.");
                }
                srcAcc.setAmount(srcAcc.getAmount() - amount);
                break;
            case "TRANSFER":
                Account targetAcc = accountRepository.findByAccountNo(targetAccountNo);
                if (targetAcc == null) {
                    throw new AccountNotFound("Target account: "+targetAccountNo+" not found.");
                }
                if (targetAccountNo == null) {
                    throw new IllegalArgumentException("Target account must be provided for transfer");
                }
                if(srcAcc.getAmount() < amount){
                    throw new InsufficientBalanceException("Insufficient balance. Please try again.");
                }

                srcAcc.setAmount(srcAcc.getAmount() - amount);
                targetAcc.setAmount(targetAcc.getAmount() + amount);
                accountRepository.save(targetAcc);
                break;
            default:
                throw new IllegalArgumentException("Invalid transaction type: " + type);
        }

        accountRepository.save(srcAcc);

        Map<String, Object> response = new java.util.HashMap<>();
        response.put("Account_Number", srcAcc.getAccountNo());
        response.put("balance", srcAcc.getAmount());
        return response;
    }
}
