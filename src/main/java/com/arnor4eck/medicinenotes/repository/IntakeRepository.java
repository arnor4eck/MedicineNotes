package com.arnor4eck.medicinenotes.repository;

import com.arnor4eck.medicinenotes.entity.Intake;
import com.arnor4eck.medicinenotes.entity.IntakesStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface IntakeRepository extends JpaRepository<Intake,Long> {
    @Query(value = "SELECT i.* FROM intakes AS i\n" +
            "JOIN templates AS t on t.id = i.template_id\n" +
            "JOIN users AS u on u.id = t.user_id\n" +
            "WHERE u.email = :email\n" +
            "ORDER BY adoptedin", nativeQuery = true)
    Collection<Intake> findAllByCreatorEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE intakes AS i SET status = :status\n" +
            "WHERE id = :id AND EXISTS(SELECT 1 FROM intakes AS i\n" +
            "    JOIN templates AS t ON t.id = i.template_id\n" +
            "    JOIN public.users AS u on u.id = t.user_id\n" +
            "    WHERE u.email = :email  AND i.id = :id)", nativeQuery = true) // подзапрос - провер
    int setStatus(@Param("id") long id,
                  @Param("status") String status,
                  @Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE intakes AS i SET status = 'DONE', adoptedIn = NOW()\n" +
            "WHERE id = :id AND EXISTS(SELECT 1 FROM intakes AS i\n" +
            "    JOIN templates AS t ON t.id = i.template_id\n" +
            "    JOIN public.users AS u on u.id = t.user_id\n" +
            "    WHERE u.email = :email  AND i.id = :id)", nativeQuery = true)
    int setDone(@Param("id") long id,
                @Param("email") String email);
}
