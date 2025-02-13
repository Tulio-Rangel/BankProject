package com.tulio.banksofka.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Data
@Getter
@Setter
public class UserReference {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Para auto-incremento
    private Long id;
    private String userId;  // ID del usuario en auth-service
    private String name;
    private String email;

    @OneToMany(mappedBy = "userReference", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<BankAccount> accounts;
}