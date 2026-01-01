package org.example.controller;

import org.example.model.AmountDTO;
import org.example.model.Transaction;
import org.example.model.TransferDTO;
import org.example.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping(value = "/account/{accNumber}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable String accNumber) {
        List<Transaction> list = transactionService.findTransactionBySourceAccount(accNumber);
        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    @PostMapping(value = "/deposit")
    public ResponseEntity<String> deposit(@RequestBody AmountDTO amountDTO) {
        String msg = transactionService.depositAmount(amountDTO.getAccountNo(), amountDTO.getAmount());
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PostMapping(value = "/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody AmountDTO amountDTO) {
        String msg = transactionService.withdrawAmount(amountDTO.getAccountNo(), amountDTO.getAmount());
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }

    @PostMapping(value = "/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferDTO transferDTO) {
        String msg = transactionService.transfer(transferDTO);
        return ResponseEntity.status(HttpStatus.OK).body(msg);
    }
}
