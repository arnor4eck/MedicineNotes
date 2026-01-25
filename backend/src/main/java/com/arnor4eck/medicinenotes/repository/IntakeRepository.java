package com.arnor4eck.medicinenotes.repository;

import com.arnor4eck.medicinenotes.entity.Intake;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface IntakeRepository extends JpaRepository<Intake,Long> {
    @Query(value = """
            SELECT i.* FROM intakes AS i
            JOIN templates AS t on t.id = i.template_id
            JOIN users AS u on u.id = t.user_id
            WHERE u.email = :email
            ORDER BY adoptedin""", nativeQuery = true)
    Collection<Intake> findAllByCreatorEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE intakes SET status = 'SKIPPED'\n" +
            "WHERE shouldAdoptedin < (CURRENT_TIMESTAMP AT TIME ZONE 'Europe/Moscow')::date\n" +
            "AND status = 'PENDING'", nativeQuery = true)
    int setSkipped();
}
