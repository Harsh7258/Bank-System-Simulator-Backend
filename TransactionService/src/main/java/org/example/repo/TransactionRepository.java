package org.example.repo;

import org.example.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction,String> {
    List<Transaction> findTransactionBySourceAccountOrderByTimestampDesc(String srcAcc);
}
