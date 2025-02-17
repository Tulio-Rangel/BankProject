package com.tulio.banksofka.service;

import com.tulio.banksofka.dto.BalanceDTO;
import com.tulio.banksofka.dto.TransactionDTO;
import com.tulio.banksofka.exception.InsufficientBalanceException;
import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.Transaction;
import com.tulio.banksofka.model.UserReference;
import com.tulio.banksofka.repository.BankAccountRepository;
import com.tulio.banksofka.repository.TransactionRepository;
import com.tulio.banksofka.repository.UserReferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class AccountService {
    private final BankAccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserReferenceRepository userReferenceRepository;

    private static final String CUENTA_NO_ENCONTRADA = "Cuenta no encontrada";
    private static final Random RANDOM = new Random();


    public AccountService(BankAccountRepository accountRepository, TransactionRepository transactionRepository, UserReferenceRepository userReferenceRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.userReferenceRepository = userReferenceRepository;
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
        return String.valueOf(RANDOM.nextInt(9000000) + 1000000);
    }

    // Modificación: Función pura, consulta la base de datos pero no modifica estado.
    private boolean isAccountNumberInUse(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }


    /*public BankAccount createAccount(String userId) {
        UserReference user = userReferenceRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        BankAccount account = new BankAccount();
        account.setUserReference(user);
        account.setBalance(0.0);
        account.setAccountNumber(generateAccountNumber());
        return accountRepository.save(account);
    }*/
    public BankAccount createAccount(UserReference userReference) {
        BankAccount account = new BankAccount();
        account.setUserReference(userReference);
        account.setBalance(0.0);
        account.setAccountNumber(generateAccountNumber());
        return accountRepository.save(account);
    }

    private Transaction createTransaction(String type, Double amount, BankAccount account) {
        return new Transaction(type, amount, LocalDateTime.now(), account);
    }

    public HashMap<String, Object> makeDeposit(Long accountId, Double amount) {
        // Mapa necesario para los detalles de la transacción
        HashMap<String, Object> depositDetails = new HashMap<>();
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException(CUENTA_NO_ENCONTRADA));

        Double initialBalance = account.getBalance();
        account.setBalance(initialBalance + amount);
        accountRepository.save(account);

        Transaction transaction = createTransaction("DEPOSIT", amount, account);
        transactionRepository.save(transaction);

        depositDetails.put("UserId", account.getUserReference().getUserId());
        depositDetails.put("InitialBalance", initialBalance);
        depositDetails.put("Amount", amount);
        depositDetails.put("FinalBalance", account.getBalance());
        return depositDetails;
    }

    public HashMap<String, Object>  makeWithdrawal(Long accountId, Double amount) {
        HashMap<String, Object> withdrawalDetails = new HashMap<>();
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException(CUENTA_NO_ENCONTRADA));

        if (account.getBalance() < amount) {
            throw new InsufficientBalanceException("Saldo insuficiente");
        }

        Double initialBalance = account.getBalance();
        account.setBalance(initialBalance - amount);
        accountRepository.save(account);

        Transaction transaction = createTransaction("WITHDRAWAL", amount, account);
        transactionRepository.save(transaction);

        withdrawalDetails.put("UserId", account.getUserReference().getUserId());
        withdrawalDetails.put("InitialBalance", initialBalance);
        withdrawalDetails.put("Amount", amount);
        withdrawalDetails.put("FinalBalance", account.getBalance());
        return withdrawalDetails;
    }

    // Modificación: Composición de funciones para transformar y ordenar datos de manera funcional.
    public List<TransactionDTO> getTransactionHistory(Long accountId) {
        return accountRepository.findById(accountId)
                .map(BankAccount::getTransactions)
                .orElseThrow(() -> new RuntimeException(CUENTA_NO_ENCONTRADA))
                .stream()
                .map(TransactionDTO::new)
                .sorted(Comparator.comparing(TransactionDTO::getDate).reversed()) // Orden por fecha descendente
                .toList();
    }

    public BalanceDTO getBalanceInfo(Long accountId) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException(CUENTA_NO_ENCONTRADA));
        return new BalanceDTO(account.getAccountNumber(), account.getBalance());
    }

    public List<BankAccount> getAccountsByUserId(String userId) {
        UserReference user = userReferenceRepository.findByUserId(userId);
		if (user == null) {
	        throw new RuntimeException("Usuario no encontrado");
	    }
		List<BankAccount> accounts = user.getAccounts();
		System.out.println(accounts); // TODO: User with no accounts
		
        return accounts;
    }

}

