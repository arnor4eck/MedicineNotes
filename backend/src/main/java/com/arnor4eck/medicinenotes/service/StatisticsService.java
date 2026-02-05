package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import com.arnor4eck.medicinenotes.repository.IntakeRepository;
import com.arnor4eck.medicinenotes.util.statistics.FullTemplateStatistics;
import com.arnor4eck.medicinenotes.util.statistics.IntakeStatisticsUnit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final IntakeRepository intakeRepository;

    private final MedicineTemplateService templateService;

    public Collection<IntakeStatisticsUnit> getIntakeStatisticsByDateAndEmail(LocalDate date,
                                                                              String email){
        return intakeRepository.getIntakeStatisticsByDate(date, email);
    }

    public FullTemplateStatistics getFullTemplateStatisticsByIdAndEmail(long templateId, int offset,
                                                                        String email){

        MedicineTemplate template = templateService.getTemplateByIdCreator(templateId, email);

        return new FullTemplateStatistics(intakeRepository.getTemplateStatisticsOfDone(templateId, offset), template.getQuantityPerDay());
    }
}
