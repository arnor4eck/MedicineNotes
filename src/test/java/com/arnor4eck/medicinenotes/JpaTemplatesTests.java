package com.arnor4eck.medicinenotes;

import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import com.arnor4eck.medicinenotes.repository.IntakeRepository;
import com.arnor4eck.medicinenotes.repository.TemplateRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.time.LocalDate;

@DataJpaTest
public class JpaTemplatesTests {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private TemplateRepository templateRepository;

    @Test
    public void testCreatingTemplate(){
        MedicineTemplate template = MedicineTemplate.builder()
                .description("This is a test template")
                .name("Test")
                .quantityPerDay(2)
                .until(LocalDate.MAX)
                .build();

        templateRepository.save(template);

        testEntityManager.flush(); // очистка кеша
        testEntityManager.clear();

        Assertions.assertNotNull(templateRepository.findById(template.getId()).get());
    }
}
