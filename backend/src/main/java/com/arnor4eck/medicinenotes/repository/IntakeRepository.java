package com.arnor4eck.medicinenotes.repository;

import com.arnor4eck.medicinenotes.entity.Intake;
import com.arnor4eck.medicinenotes.util.statistics.IntakeStatisticsUnit;
import com.arnor4eck.medicinenotes.util.statistics.TemplateStatisticsUnit;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
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

    @Query(value = """
        SELECT t.name, i.status, COUNT(*) FROM intakes AS i
        JOIN templates AS t ON t.id = i.template_id
        JOIN users AS u On u.id = t.user_id
        WHERE i.shouldadoptedin = :date AND u.email = :email
        GROUP BY (t.name, i.status)
        ORDER BY (t.name)
    """, nativeQuery = true)
    Collection<IntakeStatisticsUnit> getIntakeStatisticsByDate(@Param("date") LocalDate date,
                                                               @Param("email") String email);

    @Query(value = """
    SELECT i.shouldadoptedin, COUNT(*) FILTER (WHERE i.status = 'DONE')
    FROM intakes AS i
    WHERE i.template_id = :template_id
    GROUP BY (i.shouldadoptedin)
    ORDER BY (i.shouldadoptedin)
    LIMIT 5 OFFSET 5 * :offset;
    """,  nativeQuery = true)
    Collection<TemplateStatisticsUnit> getTemplateStatisticsOfDone(@Param("template_id") long templateId,
                                                                   @Param("offset") int offsetWith5);
}
