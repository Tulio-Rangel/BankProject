package com.tulio.banksofka.repository;

import com.tulio.banksofka.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
