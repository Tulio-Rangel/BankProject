package com.tulio.banksofka.service;

import com.tulio.banksofka.model.UserReference;
import com.tulio.banksofka.repository.UserReferenceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserReferenceService {

    private final UserReferenceRepository userReferenceRepository;

    public UserReferenceService(UserReferenceRepository userReferenceRepository) {
        this.userReferenceRepository = userReferenceRepository;
    }

    public UserReference createOrGetUserReference(String userId, String name, String email) {
        log.info("Creating or getting UserReference for userId: {}, name: {}, email: {}", userId, name, email);

        UserReference existingUser = userReferenceRepository.findByUserId(userId);
        if (existingUser != null) {
            log.info("Found existing user: {}", existingUser);

            // Actualizar datos si han cambiado
            if (!name.equals(existingUser.getName()) || !email.equals(existingUser.getEmail())) {
                log.info("Updating existing user information");
                existingUser.setName(name);
                existingUser.setEmail(email);
                existingUser = userReferenceRepository.save(existingUser);
                log.info("Updated user: {}", existingUser);
            }

            return existingUser;
        }

        log.info("Creating new UserReference");
        UserReference newUser = new UserReference();
        newUser.setUserId(userId);
        newUser.setName(name);
        newUser.setEmail(email);

        log.info("About to save new user with values: {}", newUser);
        UserReference savedUser = userReferenceRepository.save(newUser);
        log.info("Saved new user: {}", savedUser);

        if (savedUser.getUserId() == null || savedUser.getName() == null || savedUser.getEmail() == null) {
            log.error("Saved user has null values! userId: {}, name: {}, email: {}",
                    savedUser.getUserId(), savedUser.getName(), savedUser.getEmail());
        }

        return savedUser;
    }
}