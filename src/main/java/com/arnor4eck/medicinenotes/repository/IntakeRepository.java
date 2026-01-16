package com.arnor4eck.medicinenotes.repository;

import com.arnor4eck.medicinenotes.entity.Intake;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntakeRepository extends JpaRepository<Intake,Long> {
}
