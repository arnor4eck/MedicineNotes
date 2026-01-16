package com.arnor4eck.medicinenotes.repository;

import com.arnor4eck.medicinenotes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
