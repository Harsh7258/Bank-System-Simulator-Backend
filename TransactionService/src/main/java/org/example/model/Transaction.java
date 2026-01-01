package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "transactions")
public class Transaction {
    @Id
    private String id;

    private String transactionId;
    private String sourceAccount;
    private TransType tranType;
    private Status status;
    private double tranAmount;
    private Date timestamp;
    private String destinationAccount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(String sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public double getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(double tranAmount) {
        this.tranAmount = tranAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TransType getTranType() {
        return tranType;
    }

    public void setTranType(TransType tranType) {
        this.tranType = tranType;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public void setDestinationAccount(String destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
}
