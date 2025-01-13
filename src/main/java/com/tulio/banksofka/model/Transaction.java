package com.tulio.banksofka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // DEPOSIT or WITHDRAWAL
    private Double amount;
    private LocalDateTime date;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "account_id")
    private BankAccount account;

    public Transaction(String type, Double amount, LocalDateTime now, BankAccount account) {
        this.type = type;
        this.amount = amount;
        this.date = now;
        this.account = account;
    }
}
