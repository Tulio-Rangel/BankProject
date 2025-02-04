package com.tulio.banksofka.controller;

import com.tulio.banksofka.dto.BalanceDTO;
import com.tulio.banksofka.dto.TransactionDTO;
import com.tulio.banksofka.dto.TransactionRequest;
import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.User;
import com.tulio.banksofka.service.AccountService;
import com.tulio.banksofka.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BankController {
    private final UserService userService;

    private final AccountService accountService;

    public BankController(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @PostMapping("/accounts/{userId}")
    public ResponseEntity<BankAccount> createAccount(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(accountService.createAccount(user));
    }

    @GetMapping("/accounts/{accountId}/balance")
    public ResponseEntity<BalanceDTO> getBalance(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getBalanceInfo(accountId));
    }

    @PostMapping("/accounts/{accountId}/deposit")
    public ResponseEntity<Void> makeDeposit(
            @PathVariable Long accountId,
            @RequestBody TransactionRequest deposit) {
        accountService.makeDeposit(accountId, deposit.getAmount());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accounts/{accountId}/withdrawal")
    public ResponseEntity<Void> makeWithdrawal(
            @PathVariable Long accountId,
            @RequestBody TransactionRequest withdrawal) {
        accountService.makeWithdrawal(accountId, withdrawal.getAmount());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactionHistory(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getTransactionHistory(accountId));
    }

}