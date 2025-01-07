package com.tulio.banksofka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
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
}
