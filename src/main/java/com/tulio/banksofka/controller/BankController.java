package com.tulio.banksofka.controller;

import com.tulio.banksofka.dto.BalanceDTO;
import com.tulio.banksofka.dto.TransactionDTO;
import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.User;
import com.tulio.banksofka.service.AccountService;
import com.tulio.banksofka.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BankController {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User userDetails) {
        User user = userService.findById(userId);
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        // Si se proporciona una nueva contraseña, actualizarla
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(userDetails.getPassword());
        }
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<BankAccount>> getUserAccounts(@PathVariable Long userId) {
        User user = userService.findById(userId);
        return ResponseEntity.ok(user.getAccounts());
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
            @RequestParam Double amount) {
        accountService.makeDeposit(accountId, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accounts/{accountId}/withdrawal")
    public ResponseEntity<Void> makeWithdrawal(
            @PathVariable Long accountId,
            @RequestParam Double amount) {
        accountService.makeWithdrawal(accountId, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactionHistory(@PathVariable Long accountId) {
        return ResponseEntity.ok(accountService.getTransactionHistory(accountId));
    }

}