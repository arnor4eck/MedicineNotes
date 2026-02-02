package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.repository.IntakeRepository;
import com.arnor4eck.medicinenotes.util.statistics.IntakeStatisticsUnit;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Collection;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final IntakeRepository intakeRepository;

    public Collection<IntakeStatisticsUnit> getIntakeStatisticsByDateAndEmail(LocalDate date,
                                                                              String email){
        return intakeRepository.getIntakeStatisticsByDate(date, email);
    }
}
