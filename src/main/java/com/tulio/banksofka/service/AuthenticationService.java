package com.tulio.banksofka.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class AuthenticationService {
    @Value("${auth.service.url}")
    private String authServiceUrl;

    public boolean validateToken(String token) {
        try {
            URL url = new URL(authServiceUrl + "/api/auth/validate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // Leer y parsear la respuesta
                // Retornar true si el token es válido
                return true;
            }
        } catch (Exception e) {
            log.error("Error validating token", e);
        }
        return false;
    }
}
