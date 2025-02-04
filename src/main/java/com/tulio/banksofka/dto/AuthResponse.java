package com.tulio.banksofka.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private Long id;

    public AuthResponse(String token, String name, String email, Long id) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.id = id;
    }
}
