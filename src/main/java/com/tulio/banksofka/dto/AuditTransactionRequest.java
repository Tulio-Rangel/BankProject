package com.tulio.banksofka.dto;

public class AuditTransactionRequest {
    private String userId;
    private Double initialBalance;
    private Double amount;
    private Double finalBalance;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(Double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(Double finalBalance) {
        this.finalBalance = finalBalance;
    }
}