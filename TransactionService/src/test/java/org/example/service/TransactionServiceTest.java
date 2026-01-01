package org.example.service;

import org.example.client.AccountClient;
import org.example.model.NotificationDTO;
import org.example.model.Status;
import org.example.model.TransType;
import org.example.model.TransferDTO;
import org.example.repo.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountClient accountClient;

    @Mock
    private NotificationServiceCaller notificationServiceCaller;

    @Test
    void depositAmount_success() {

        String accountNo = "ACC123";
        double amount = 1000.0;

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");

        when(accountClient.updateBalance(
                accountNo, amount, "CREDIT", null))
                .thenReturn(response);

        String result = transactionService.depositAmount(accountNo, amount);

        assertEquals("Amount deposited successfully", result);

        verify(accountClient, times(1))
                .updateBalance(accountNo, amount, "CREDIT", null);
    }

    @Test
    void withdrawAmount_success() {

        String accountNo = "ACC456";
        double amount = 500.0;

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");

        when(accountClient.updateBalance(
                accountNo, amount, "DEBIT", null))
                .thenReturn(response);

        String result = transactionService.withdrawAmount(accountNo, amount);

        assertEquals("Amount withdrawn successfully", result);

        verify(accountClient).updateBalance(
                accountNo, amount, "DEBIT", null);

        verify(transactionRepository).save(argThat(txn ->
                txn.getStatus() == Status.SUCCESS &&
                        txn.getTranType() == TransType.DEBIT
        ));

        verify(notificationServiceCaller).send(any(NotificationDTO.class));
    }

    @Test
    void transfer_success() {

        TransferDTO dto = new TransferDTO();
        dto.setSrcAccount("ACC123");
        dto.setDestAccount("ACC222");
        dto.setAmount(200.0);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");

        when(accountClient.updateBalance(
                "ACC123", 200.0, "TRANSFER", "ACC222"))
                .thenReturn(response);

        String result = transactionService.transfer(dto);

        assertTrue(result.contains("Transfer successful"));

        verify(accountClient).updateBalance(
                "ACC123", 200.0, "TRANSFER", "ACC222");

        verify(transactionRepository).save(argThat(txn ->
                txn.getStatus() == Status.SUCCESS &&
                        txn.getTranType() == TransType.TRANSFER
        ));

        verify(notificationServiceCaller).send(any(NotificationDTO.class));
    }

}
