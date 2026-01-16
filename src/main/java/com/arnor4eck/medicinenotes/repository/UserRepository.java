package com.arnor4eck.medicinenotes.repository;

import com.arnor4eck.medicinenotes.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
