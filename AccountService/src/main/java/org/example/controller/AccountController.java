package org.example.controller;

import org.example.model.Account;
import org.example.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping(value="/{accNumber}")
    public ResponseEntity<Account> getAccount(@PathVariable String accNumber) {
        Account acc = accountService.findByAccountNumber(accNumber);
        return ResponseEntity.status(HttpStatus.OK).body(acc);
    }

    @PostMapping(value = "/")
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        String acc = accountService.createAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(acc);
    }

    @PutMapping(value = "/{accNumber}/status/{status}")
    public ResponseEntity<String> changeStatus(@PathVariable String accNumber, @PathVariable String status) {
        String msg = accountService.changeStatus(accNumber, status);
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    // doubt in this design
    @PutMapping(value = "/{accNumber}/balance")
    public ResponseEntity<Map<String, Object>> updateBalance(
            @PathVariable String accNumber,
            @RequestParam("amount") double amount,
            @RequestParam("type") String type,
            @RequestParam(value = "targetAccountNo", required = false) String targetAccountNo) {

        Map<String, Object> response = accountService.updateBalance(accNumber, amount, type, targetAccountNo);
        return ResponseEntity.ok(response);
    }
}
