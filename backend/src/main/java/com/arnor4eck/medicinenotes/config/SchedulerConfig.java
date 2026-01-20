package com.arnor4eck.medicinenotes.config;

import com.arnor4eck.medicinenotes.repository.IntakeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Slf4j
@AllArgsConstructor
@Component
public class SchedulerConfig {

    private final IntakeRepository intakeRepository;

    @Scheduled(cron = "0 1 0 * * ?", zone = "Europe/Moscow")
    public void scheduled() {
        log.info("Запуск Scheduler");

        int updatedRows = intakeRepository.setSkipped();

        log.info("Изменено {} строк", updatedRows);
        if(updatedRows == 0)
            log.warn("Было изменено 0 строк, проверьте базу");
    }
}
