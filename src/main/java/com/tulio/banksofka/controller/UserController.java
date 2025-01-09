package com.tulio.banksofka.controller;

import com.tulio.banksofka.model.BankAccount;
import com.tulio.banksofka.model.User;
import com.tulio.banksofka.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


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
}
