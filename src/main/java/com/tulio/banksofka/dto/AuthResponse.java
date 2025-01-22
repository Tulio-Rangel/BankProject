package com.tulio.banksofka.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String name;
    private Long id;

    public AuthResponse(String token, String name, Long id) {
        this.token = token;
        this.name = name;
        this.id = id;
    }
}
