package com.tulio.banksofka.controller;

import com.tulio.banksofka.service.MessageProducerService;
import com.tulio.banksofka.dto.BalanceDTO;
import com.tulio.banksofka.dto.TransactionDTO;
import com.tulio.banksofka.dto.TransactionRequest;
import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.UserReference;
import com.tulio.banksofka.security.JwtUtil;
import com.tulio.banksofka.service.AccountService;
import com.tulio.banksofka.service.UserReferenceService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api")
public class BankController {

    private final AccountService accountService;
    private final UserReferenceService userReferenceService;
    private final JwtUtil jwtUtil;
    private final MessageProducerService messageProducerService;

    /*
     * public BankController(AccountService accountService) {
     * this.accountService = accountService;
     * }
     */

    public BankController(AccountService accountService,
            UserReferenceService userReferenceService,
            JwtUtil jwtUtil,
            MessageProducerService messageProducerService) {
        this.accountService = accountService;
        this.userReferenceService = userReferenceService;
        this.jwtUtil = jwtUtil;
        this.messageProducerService = messageProducerService;
    }

    /*
     * @CrossOrigin(origins = "http://localhost:4200")
     * 
     * @PostMapping("/accounts/{userId}")
     * public ResponseEntity<BankAccount> createAccount(@PathVariable String userId)
     * {
     * return ResponseEntity.ok(accountService.createAccount(userId));
     * }
     */

    @PostMapping("/accounts")
    public ResponseEntity<?> createAccount(@RequestHeader("Authorization") String authHeader) {
        String userId = null;
        try {
            String token = authHeader.substring(7);
            log.info("Received token: {}", token);

            Claims claims = jwtUtil.extractAllClaims(token);
            log.info("Extracted claims from token: {}", claims);

            userId = claims.get("userId", String.class);
            String name = claims.get("name", String.class);
            String email = claims.get("email", String.class);

            log.info("Extracted user data from token - userId: {}, name: {}, email: {}",
                    userId, name, email);

            if (userId == null || name == null || email == null) {
                log.error("Required claims are missing from token");
                messageProducerService.sendMessageAccount("account.creation", "Error en creación de cuenta para usuario", userId, false);
                return ResponseEntity.badRequest().body("Invalid token: missing required claims");
            }

            UserReference userReference = userReferenceService.createOrGetUserReference(userId, name, email);
            BankAccount account = accountService.createAccount(userReference);

            log.info("Created account with userReference - id: {}, userId: {}, name: {}, email: {}",
                    userReference.getId(), userReference.getUserId(),
                    userReference.getName(), userReference.getEmail());

            messageProducerService.sendMessageAccount("account.creation", "Cuenta de banco creada", userId, true);

            return ResponseEntity.ok(account);
        } catch (Exception e) {
            log.error("Error creating account", e);
            messageProducerService.sendMessageAccount("account.creation", "Error en creación de cuenta", userId, false);
            return ResponseEntity.badRequest().body("Error creating account: " + e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/accounts/{accountId}/balance")
    public ResponseEntity<BalanceDTO> getBalance(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getBalanceInfo(accountId));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/accounts/{accountId}/deposit")
    public ResponseEntity<Void> makeDeposit(
            @PathVariable Long accountId,
            @RequestBody TransactionRequest deposit) {
        HashMap<String, Object> depositDetails = null;
        try {
            depositDetails = accountService.makeDeposit(accountId, deposit.getAmount());
            messageProducerService.sendMessageTransaction("transaction.deposit", "Depósito realizado en la cuenta " + accountId, true, depositDetails);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            depositDetails.put("message", e.getMessage());
            messageProducerService.sendMessageTransaction("transaction.deposit", "Error en depósito en la cuenta " + accountId, false, depositDetails);
            return ResponseEntity.badRequest().build();
        }

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/accounts/{accountId}/withdrawal")
    public ResponseEntity<Void> makeWithdrawal(
            @PathVariable Long accountId,
            @RequestBody TransactionRequest withdrawal) {
        HashMap<String, Object> withdrawalDetails = null;
        try {
            withdrawalDetails = accountService.makeWithdrawal(accountId, withdrawal.getAmount());

            messageProducerService.sendMessageTransaction("transaction.withdrawal", "Retiro realizado en la cuenta " + accountId, true, withdrawalDetails);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            withdrawalDetails.put("message", e.getMessage());
            messageProducerService.sendMessageTransaction("transaction.withdrawal", "Error en retiro en la cuenta " + accountId, false, withdrawalDetails);
            return ResponseEntity.badRequest().build();
        }

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactionHistory(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getTransactionHistory(accountId));
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<BankAccount>> getUserAccounts(@PathVariable String userId) {
        return ResponseEntity.ok(accountService.getAccountsByUserId(userId));
    }

}