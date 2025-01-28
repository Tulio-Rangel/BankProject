package com.tulio.banksofka.service;

import com.tulio.banksofka.dto.AuditTransactionRequest;
import com.tulio.banksofka.dto.BalanceDTO;
import com.tulio.banksofka.dto.TransactionDTO;
import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.Transaction;
import com.tulio.banksofka.model.User;
import com.tulio.banksofka.repository.BankAccountRepository;
import com.tulio.banksofka.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

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
    private final WebClient webClient;

    public AccountService(BankAccountRepository accountRepository, TransactionRepository transactionRepository, WebClient webClient) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.webClient = webClient;
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

    public void makeDeposit(Long accountId, Double amount) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        Double initialBalance = account.getBalance();
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Transaction transaction = createTransaction("DEPOSIT", amount, account);
        transactionRepository.save(transaction);

        // Llamar al Proyecto 2 para registrar la auditoría
        AuditTransactionRequest request = new AuditTransactionRequest();
        request.setUserId(account.getUser().getId().toString());
        request.setInitialBalance(initialBalance);
        request.setAmount(amount);
        request.setFinalBalance(account.getBalance());

        webClient.post()
                .uri("/api/audit/deposits")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .subscribe(); // Ejecutar de forma asíncrona
    }

    public void makeWithdrawal(Long accountId, Double amount) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (account.getBalance() < amount) {
            throw new RuntimeException("Saldo insuficiente");
        }

        Double initialBalance = account.getBalance();
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);

        Transaction transaction = createTransaction("WITHDRAWAL", amount, account);
        transactionRepository.save(transaction);

        // Llamar al Proyecto 2 para registrar la auditoría
        AuditTransactionRequest request = new AuditTransactionRequest();
        request.setUserId(account.getUser().getId().toString());
        request.setInitialBalance(initialBalance);
        request.setAmount(amount);
        request.setFinalBalance(account.getBalance());

        webClient.post()
                .uri("/api/audit/withdrawals")
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .subscribe(); // Ejecutar de forma asíncrona
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

