package com.tulio.banksofka.service;

import com.tulio.banksofka.dto.BalanceDTO;
import com.tulio.banksofka.dto.TransactionDTO;
import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.Transaction;
import com.tulio.banksofka.model.User;
import com.tulio.banksofka.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {
    @Autowired
    private BankAccountRepository accountRepository;

    private static final int ACCOUNT_NUMBER_LENGTH = 10;
    private static final int MAX_RETRIES = 5;

    private String generateAccountNumber() {
        for (int i = 0; i < MAX_RETRIES; i++) {
            String accountNumber = createRandomAccountNumber();
            if (!accountRepository.existsByAccountNumber(accountNumber)) {
                return accountNumber;
            }
        }
        throw new RuntimeException("No se pudo generar un número de cuenta único después de varios intentos");
    }

    private String createRandomAccountNumber() {
        StringBuilder accountNumber = new StringBuilder(ACCOUNT_NUMBER_LENGTH);

        accountNumber.append((char) ('1' + new Random().nextInt(9)));

        for (int i = 1; i < ACCOUNT_NUMBER_LENGTH; i++) {
            accountNumber.append((char) ('0' + new Random().nextInt(10)));
        }

        return accountNumber.toString();
    }


    public BankAccount createAccount(User user) {
        BankAccount account = new BankAccount();
        account.setUser(user);
        account.setBalance(0.0);
        account.setAccountNumber(generateAccountNumber());
        return accountRepository.save(account);
    }

    public Double getBalance(Long accountId) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return account.getBalance();
    }

    public void makeDeposit(Long accountId, Double amount) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        account.setBalance(account.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setAccount(account);

        account.getTransactions().add(transaction);
        accountRepository.save(account);
    }

    public void makeWithdrawal(Long accountId, Double amount) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Saldo insuficiente");
        }

        account.setBalance(account.getBalance() - amount);

        Transaction transaction = new Transaction();
        transaction.setType("WITHDRAWAL");
        transaction.setAmount(amount);
        transaction.setDate(LocalDateTime.now());
        transaction.setAccount(account);

        account.getTransactions().add(transaction);
        accountRepository.save(account);
    }

    public List<TransactionDTO> getTransactionHistory(Long accountId) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        return account.getTransactions().stream()
                .map(TransactionDTO::new)
                .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                .collect(Collectors.toList());
    }

    public BalanceDTO getBalanceInfo(Long accountId) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        return new BalanceDTO(account.getAccountNumber(), account.getBalance());
    }

}

