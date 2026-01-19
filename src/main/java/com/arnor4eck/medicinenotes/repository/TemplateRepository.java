package com.arnor4eck.medicinenotes.repository;

import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface TemplateRepository extends JpaRepository<MedicineTemplate,Long> {
    @Query(value = "SELECT t.* FROM templates AS t " +
            "JOIN users AS u ON u.id = t.user_id " +
            "WHERE u.email = :email " +
            "ORDER BY until", nativeQuery = true)
    Collection<MedicineTemplate> findByCreatorEmail(@Param("email") String email);
}
