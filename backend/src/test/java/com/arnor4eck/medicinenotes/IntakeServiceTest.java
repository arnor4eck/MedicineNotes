package com.arnor4eck.medicinenotes;

import com.arnor4eck.medicinenotes.repository.IntakeRepository;
import com.arnor4eck.medicinenotes.service.MedicineTemplateService;
import com.arnor4eck.medicinenotes.service.StatisticsService;
import com.arnor4eck.medicinenotes.util.statistics.IntakeStatisticsUnit;
import com.arnor4eck.medicinenotes.util.statistics.IntakesStatisticsByDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IntakeServiceTest {

    @InjectMocks
    private StatisticsService statisticsService;

    @Mock
    private IntakeRepository intakeRepository;

    @Mock
    private MedicineTemplateService medicineTemplateService;

    @Test
    public void testShouldReturnSameDistinctSize(){

        List<IntakeStatisticsUnit> returned = new ArrayList<>(Arrays.asList(
                new IntakeStatisticsUnit("name1", "DONE", 1),
                new IntakeStatisticsUnit("name1", "SKIPPED", 3),
                new IntakeStatisticsUnit("name2", "DONE", 1),
                new IntakeStatisticsUnit("name3", "SKIPPED", 7),
                new IntakeStatisticsUnit("name3", "DONE", 1)
        ));

        long distinctCount = returned.stream().map(IntakeStatisticsUnit::name)
                .distinct().count();

        when(intakeRepository.getIntakeStatisticsByDate(any(), anyString())).thenReturn(returned);

        IntakesStatisticsByDate ans = statisticsService
                .getIntakeStatisticsByDateAndEmail(LocalDate.now(), "test@mail.ru");

        Assertions.assertEquals(ans.statisticsByDate().size(), distinctCount);
    }
}
