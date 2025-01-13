package com.tulio.banksofka.repository;

import com.tulio.banksofka.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
