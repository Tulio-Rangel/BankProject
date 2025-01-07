package com.tulio.banksofka.dto;

import com.tulio.banksofka.model.Transaction;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Long id;
    private String type;
    private Double amount;
    private LocalDateTime date;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
    }
}
