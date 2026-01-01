package org.example.service;

import org.example.exception.AccountNotFound;
import org.example.exception.WrongInputException;
import org.example.model.AccStatus;
import org.example.model.Account;
import org.example.repo.AccountRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepo accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setAccountNo("ACC123");
        account.setHolderName("John");
        account.setAmount(1000);
        account.setStatus(AccStatus.ACTIVE);
    }

    @Test
    void findAll_shouldReturnAllAccounts() {
        List<Account> accounts = List.of(account);
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.findAll();

        assertEquals(1, result.size());
        verify(accountRepository).findAll();
    }

    @Test
    void findByAccountNumber_shouldReturnAccount() {
        when(accountRepository.findByAccountNo("ACC123")).thenReturn(account);

        Account result = accountService.findByAccountNumber("ACC123");

        assertNotNull(result);
        assertEquals("ACC123", result.getAccountNo());
    }

    @Test
    void findByAccountNumber_exception() {
        when(accountRepository.findByAccountNo("ACC999")).thenReturn(null);

        assertThrows(AccountNotFound.class,
                () -> accountService.findByAccountNumber("ACC999"));
    }

    @Test
    void createAccount_shouldCreateAccountSuccessfully() {
        when(accountRepository.existsByAccountNo(anyString())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        String response = accountService.createAccount(account);

        assertEquals("Account created successfully.", response);
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void changeStatus_shouldActivateAccount() {
        when(accountRepository.findByAccountNo("ACC123")).thenReturn(account);

        String response = accountService.changeStatus("ACC123", "ACTIVE");

        assertEquals(AccStatus.ACTIVE, account.getStatus());
        assertTrue(response.contains("ACTIVE"));
    }

    @Test
    void changeStatus_exception_forInvalidStatus() {
        when(accountRepository.findByAccountNo("ACC123")).thenReturn(account);

        assertThrows(WrongInputException.class,
                () -> accountService.changeStatus("ACC123", "BLOCKED"));
    }

    @Test
    void updateBalance_shouldCreditAmount() {
        when(accountRepository.findByAccountNo("ACC123")).thenReturn(account);

        Map<String, Object> response =
                accountService.updateBalance("ACC123", 500, "CREDIT", null);

        assertEquals(1500.0, account.getAmount());
        verify(accountRepository).save(account);
    }

    @Test
    void updateBalance_shouldDebitAmount() {
        when(accountRepository.findByAccountNo("ACC123")).thenReturn(account);

        Map<String, Object> response =
                accountService.updateBalance("ACC123", 300, "DEBIT", null);

        assertEquals(700.0, account.getAmount());
    }

    @Test
    void updateBalance_shouldTransferAmount() {
        Account target = new Account();
        target.setAccountNo("ACC999");
        target.setAmount(500);

        when(accountRepository.findByAccountNo("ACC123")).thenReturn(account);
        when(accountRepository.findByAccountNo("ACC999")).thenReturn(target);

        Map<String, Object> response =
                accountService.updateBalance("ACC123", 200, "TRANSFER", "ACC999");

        assertEquals(800.0, account.getAmount());
        assertEquals(700.0, target.getAmount());

        verify(accountRepository).save(target);
        verify(accountRepository).save(account);
    }
}
