package com.arnor4eck.medicinenotes.service;

import com.arnor4eck.medicinenotes.entity.IntakesStatus;
import com.arnor4eck.medicinenotes.entity.MedicineTemplate;
import com.arnor4eck.medicinenotes.repository.IntakeRepository;
import com.arnor4eck.medicinenotes.util.statistics.FullTemplateStatistics;
import com.arnor4eck.medicinenotes.util.statistics.IntakesStatisticsByDate;
import com.arnor4eck.medicinenotes.util.statistics.IntakesStatisticsByDateUnit;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class StatisticsService {

    private final IntakeRepository intakeRepository;

    private final MedicineTemplateService templateService;

    public IntakesStatisticsByDate getIntakeStatisticsByDateAndEmail(LocalDate date,
                                                                              String email){
        Map<String, IntakesStatisticsByDateUnit> units = new HashMap<>();

        intakeRepository.getIntakeStatisticsByDate(date, email).forEach(el -> {
            units.compute(el.name(), (k, v) -> {
                if(v == null)
                    v = new IntakesStatisticsByDateUnit();

                v.setCountForStatus(IntakesStatus.valueOf(el.status()), el.count());

                return v;
            });
        });

        return new IntakesStatisticsByDate(units);
    }

    public FullTemplateStatistics getFullTemplateStatisticsByIdAndEmail(long templateId, int offset,
                                                                        String email){

        MedicineTemplate template = templateService.getTemplateByIdCreator(templateId, email);

        return new FullTemplateStatistics(intakeRepository.getTemplateStatisticsOfDone(templateId, offset),
                template.getQuantityPerDay());
    }
}
