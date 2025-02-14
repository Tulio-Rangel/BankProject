package com.tulio.banksofka.repository;

import com.tulio.banksofka.model.UserReference;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReferenceRepository extends JpaRepository<UserReference, String> {
	UserReference findByUserId(String userId);
}
