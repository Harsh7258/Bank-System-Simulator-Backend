package org.example.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.example.exception.AccountNotFound;
import org.example.exception.InsufficientBalanceException;
import org.springframework.stereotype.Component;

@Component
public class AccountFeignErrors implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()) {
            case 404 -> new AccountNotFound("Account not found");
            case 400 -> new InsufficientBalanceException("Insufficient balance");
            default -> new RuntimeException("Account service error");
        };
    }
}
