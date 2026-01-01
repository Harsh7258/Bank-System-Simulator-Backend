package org.example.client;

import org.example.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "AccountService", configuration = FeignConfig.class)
public interface AccountClient {
    @PutMapping("/accounts/{accNumber}/balance")
    Map<String, Object> updateBalance(
            @PathVariable("accNumber") String accNumber,
            @RequestParam("amount") double amount,
            @RequestParam("type") String type,
            @RequestParam(value = "targetAccountNo", required = false) String targetAccountNo
    );
}
