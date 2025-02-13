package com.tulio.banksofka.repository;

import com.tulio.banksofka.model.UserReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserReferenceRepository extends JpaRepository<UserReference, String> {
    Optional<UserReference> findByUserId(String userId);
}
