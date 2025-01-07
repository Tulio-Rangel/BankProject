package com.tulio.banksofka.dto;

import lombok.Data;

@Data
public class BalanceDTO {
    private String accountNumber;
    private Double balance;

    public BalanceDTO(String accountNumber, Double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
}
