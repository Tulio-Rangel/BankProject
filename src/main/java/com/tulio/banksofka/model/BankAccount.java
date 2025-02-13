package com.tulio.banksofka.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "accounts")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private Double balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserReference userReference;

    @JsonIgnore
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    // Modificación: Método inmutable que devuelve una nueva instancia con el balance actualizado.
    public BankAccount withBalance(Double newBalance) {
        return new BankAccount(this.id, this.accountNumber, newBalance, this.userReference, this.transactions);
    }

    // Constructor inmutable para apoyar el método withBalance
    public BankAccount(Long id, String accountNumber, Double balance, UserReference userReference, List<Transaction> transactions) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.userReference = userReference;
        this.transactions = new ArrayList<>(transactions); // Inmutabilidad en listas
    }
}
