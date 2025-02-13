package com.tulio.banksofka.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class UserReference {
    @Id
    private String userId;  // ID del usuario en auth-service
    private String name;
    private String email;

    @OneToMany(mappedBy = "userReference", cascade = CascadeType.ALL)
    private List<BankAccount> accounts;
}