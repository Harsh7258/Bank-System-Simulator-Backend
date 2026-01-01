package org.example.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.example.client.AccountClient;

import org.example.exception.TransactionNotFoundException;
import org.example.model.*;
import org.example.repo.TransactionRepository;
import org.example.utils.TransactionNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountClient accountFeignClient;

    @Autowired
    private NotificationServiceCaller notificationServiceCaller;

    // @desc helper method
    // @func common transaction builder for minimizing code
    private Transaction buildTransaction(String sourceAccount, double amount, TransType type) {
        Transaction txn = new Transaction();
        txn.setTransactionId(TransactionNumberGenerator.generate());
        txn.setSourceAccount(sourceAccount);
        txn.setTranAmount(amount);
        txn.setTranType(type);
        txn.setTimestamp(new Date());

        return txn;
    }

    public NotificationDTO buildNotification(String email, String accountNo, TransType type, double amount, Status status) {
        NotificationDTO request = new NotificationDTO();
        request.setToEmail(email);
        request.setAccountNo(accountNo);
        request.setTransactionType(type.name());
        request.setStatus(status.name());
        request.setAmount(amount);
        request.setMessage(getMessage(type, status));
        return request;
    }

    private String getMessage(TransType type, Status status) {

        if (status == Status.SUCCESS) {
            return switch (type) {
                case CREDIT -> "Amount credited successfully";
                case DEBIT -> "Amount debited successfully";
                case TRANSFER -> "Transfer completed successfully";
            };
        }

        return switch (type) {
            case CREDIT -> "Deposit failed. Please try again later.";
            case DEBIT -> "Withdrawal failed. Please try again later.";
            case TRANSFER -> "Transfer failed. Amount not debited.";
        };
    }

    public List<Transaction> findTransactionBySourceAccount(String srcAcc) {
        List<Transaction> transactions =
                transactionRepository.findTransactionBySourceAccountOrderByTimestampDesc(srcAcc);

        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException(
                    "No transactions found for account: " + srcAcc
            );
        }

        return transactions;
    }

    @CircuitBreaker(name = "accountServiceCB", fallbackMethod = "depositFallback")
    public String depositAmount(String accountNo, double amount) {
        Transaction txn = buildTransaction(accountNo,amount, TransType.CREDIT);

        try {
            accountFeignClient.updateBalance(
                    accountNo,
                    amount,
                    "CREDIT",
                    null
            );

            txn.setStatus(Status.SUCCESS);
            transactionRepository.save(txn);

            NotificationDTO noti = buildNotification(
                    "harshnaidu2u@gmail.com",
                    accountNo,
                    TransType.CREDIT,
                    amount,
                    Status.SUCCESS
            );

            notificationServiceCaller.send(noti);
            return "Amount deposited successfully";

        } catch (Exception ex) {
            txn.setStatus(Status.FAILED);
            txn.setTimestamp(new Date());
            transactionRepository.save(txn);

            NotificationDTO failedNoti = buildNotification(
                    "harshnaidu2u@gmail.com",
                    accountNo,
                    TransType.CREDIT,
                    amount,
                    Status.FAILED
            );

            notificationServiceCaller.send(failedNoti);

            throw ex;
        }
    }
    public String depositFallback(String accountNo, double amount, Throwable ex) {
        Transaction txn = buildTransaction(accountNo, amount, TransType.CREDIT);
        txn.setStatus(Status.FAILED);
        transactionRepository.save(txn);

        return "Account service unavailable. Deposit failed. Please try later.";
    }

    @CircuitBreaker(name = "accountServiceCB", fallbackMethod = "withdrawFallback")
    public String withdrawAmount(String accountNo, double amount) {

        Transaction txn = buildTransaction(accountNo, amount, TransType.DEBIT);

        try {
            accountFeignClient.updateBalance(
                    accountNo,
                    amount,
                    "DEBIT",
                    null
            );

            txn.setStatus(Status.SUCCESS);
            transactionRepository.save(txn);

            NotificationDTO noti = buildNotification(
                    "harshnaidu2u@gmail.com",
                    accountNo,
                    TransType.DEBIT,
                    amount,
                    Status.SUCCESS
            );

            notificationServiceCaller.send(noti);
            return "Amount withdrawn successfully";

        } catch (Exception ex) {
            txn.setStatus(Status.FAILED);
            txn.setTimestamp(new Date());
            transactionRepository.save(txn);

            NotificationDTO failedNoti = buildNotification(
                    "harshnaidu2u@gmail.com",
                    accountNo,
                    TransType.DEBIT,
                    amount,
                    Status.FAILED
            );
            notificationServiceCaller.send(failedNoti);

            throw ex;   // rethrow so client sees error
        }
    }
    public String withdrawFallback(String accountNo, double amount, Throwable ex) {
        Transaction txn = buildTransaction(accountNo, amount, TransType.DEBIT);
        txn.setStatus(Status.FAILED);
        transactionRepository.save(txn);

        return "Account service unavailable. Withdrawal failed.";
    }

    @CircuitBreaker(name = "accountServiceCB", fallbackMethod = "transferFallback")
    public String transfer(TransferDTO transferDTO) {

        Transaction txn = new Transaction();
        txn.setSourceAccount(transferDTO.getSrcAccount());
        txn.setDestinationAccount(transferDTO.getDestAccount());
        txn.setTranAmount(transferDTO.getAmount());
        txn.setTranType(TransType.TRANSFER);
        txn.setTimestamp(new Date());

        try {
            accountFeignClient.updateBalance(
                    transferDTO.getSrcAccount(),
                    transferDTO.getAmount(),
                    "TRANSFER",
                    transferDTO.getDestAccount()
            );

            txn.setStatus(Status.SUCCESS);
            transactionRepository.save(txn);

            NotificationDTO noti = buildNotification(
                    "harshnaidu2u@gmail.com",
                    transferDTO.getSrcAccount(),
                    TransType.TRANSFER,
                    transferDTO.getAmount(),
                    Status.SUCCESS
            );

            notificationServiceCaller.send(noti);
            return "Transfer successful from - " + transferDTO.getSrcAccount() + " to " + transferDTO.getDestAccount();
        } catch (Exception ex) {
            txn.setStatus(Status.FAILED);
            txn.setTimestamp(new Date());
            transactionRepository.save(txn);

            NotificationDTO failedNoti = buildNotification(
                    "harshnaidu2u@gmail.com",
                    transferDTO.getSrcAccount(),
                    TransType.TRANSFER,
                    transferDTO.getAmount(),
                    Status.FAILED
            );

            notificationServiceCaller.send(failedNoti);

            throw ex;
        }
    }
    public String transferFallback(TransferDTO transferDTO, Throwable ex) {

        Transaction txn = new Transaction();
        txn.setSourceAccount(transferDTO.getSrcAccount());
        txn.setDestinationAccount(transferDTO.getDestAccount());
        txn.setTranAmount(transferDTO.getAmount());
        txn.setTranType(TransType.TRANSFER);
        txn.setStatus(Status.FAILED);
        txn.setTimestamp(new Date());

        transactionRepository.save(txn);

        return "Transfer failed due to account service downtime.";
    }
}
