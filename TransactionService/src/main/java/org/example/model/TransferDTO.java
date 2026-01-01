package org.example.model;

public class TransferDTO {
    private String srcAccount;
    private String destAccount;
    private double amount;
    private TransType transType;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public TransType getTransType() {
        return transType;
    }

    public void setTransType(TransType transType) {
        this.transType = transType;
    }

    public String getSrcAccount() {
        return srcAccount;
    }

    public void setSrcAccount(String srcAccount) {
        this.srcAccount = srcAccount;
    }

    public String getDestAccount() {
        return destAccount;
    }

    public void setDestAccount(String destAccount) {
        this.destAccount = destAccount;
    }
}
