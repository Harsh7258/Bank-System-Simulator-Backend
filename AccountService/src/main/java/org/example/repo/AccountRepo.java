package org.example.repo;

import org.example.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepo extends MongoRepository<Account, String> {
    Account findByAccountNo(String accountNo);
    boolean existsByAccountNo(String accountNo);
}
