package com.tulio.banksofka.service;

import com.tulio.banksofka.dto.UserDTO;
import com.tulio.banksofka.model.UserReference;
import com.tulio.banksofka.repository.UserReferenceRepository;
import org.springframework.stereotype.Service;

@Service
public class UserSyncService {
    private final UserReferenceRepository userReferenceRepository;

    public UserSyncService(UserReferenceRepository userReferenceRepository) {
        this.userReferenceRepository = userReferenceRepository;
    }

    public void syncUserData(UserDTO userDTO) {
        UserReference userRef = userReferenceRepository.findById(userDTO.getId())
                .orElse(new UserReference());
        userRef.setUserId(userDTO.getId());
        userRef.setName(userDTO.getName());
        userRef.setEmail(userDTO.getEmail());
        userReferenceRepository.save(userRef);
    }

}