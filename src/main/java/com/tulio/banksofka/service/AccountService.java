package com.tulio.banksofka.service;

import com.tulio.banksofka.dto.BalanceDTO;
import com.tulio.banksofka.dto.TransactionDTO;
import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.Transaction;
import com.tulio.banksofka.model.User;
import com.tulio.banksofka.repository.BankAccountRepository;
import com.tulio.banksofka.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {
    private final BankAccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(BankAccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    // Modificación: Composición de funciones para separar validaciones y lógica de generación.
    public String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = createRandomAccountNumber();
        } while (isAccountNumberInUse(accountNumber));
        return accountNumber;
    }

    // Modificación: Función pura, depende únicamente de la entrada (random).
    private String createRandomAccountNumber() {
        return String.valueOf(new Random().nextInt(9000000) + 1000000);
    }

    // Modificación: Función pura, consulta la base de datos pero no modifica estado.
    private boolean isAccountNumberInUse(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }


    public BankAccount createAccount(User user) {
        BankAccount account = new BankAccount();
        account.setUser(user);
        account.setBalance(0.0);
        account.setAccountNumber(generateAccountNumber());
        return accountRepository.save(account);
    }

    private Transaction createTransaction(String type, Double amount, BankAccount account) {
        return new Transaction(type, amount, LocalDateTime.now(), account);
    }

    // Modificación: Uso de Optional como Mónada para evitar null checks explícitos y manejar errores.
    public void makeDeposit(Long accountId, Double amount) {
        accountRepository.findById(accountId)
                .map(account -> {
                    BankAccount updatedAccount = account.withBalance(account.getBalance() + amount);
                    Transaction depositTransaction = createTransaction("DEPOSIT", amount, updatedAccount);
                    transactionRepository.save(depositTransaction);
                    return accountRepository.save(updatedAccount);
                })
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    // Modificación: Uso de Optional como Mónada para evitar null checks explícitos y manejar errores.
    public void makeWithdrawal(Long accountId, Double amount) {
        accountRepository.findById(accountId)
                .map(account -> {
                    if (account.getBalance() < amount) {
                        throw new RuntimeException("Saldo insuficiente");
                    }
                    BankAccount updatedAccount = account.withBalance(account.getBalance() - amount);
                    Transaction withdrawalTransaction = createTransaction("WITHDRAWAL", amount, updatedAccount);
                    transactionRepository.save(withdrawalTransaction);
                    return accountRepository.save(updatedAccount);
                })
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    // Modificación: Composición de funciones para transformar y ordenar datos de manera funcional.
    public List<TransactionDTO> getTransactionHistory(Long accountId) {
        return accountRepository.findById(accountId)
                .map(BankAccount::getTransactions)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"))
                .stream()
                .map(TransactionDTO::new)
                .sorted(Comparator.comparing(TransactionDTO::getDate).reversed()) // Orden por fecha descendente
                .collect(Collectors.toList());
    }

    public BalanceDTO getBalanceInfo(Long accountId) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return new BalanceDTO(account.getAccountNumber(), account.getBalance());
    }

}

