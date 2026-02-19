package com.arnor4eck.medicinenotes.controller;

import com.arnor4eck.medicinenotes.service.StatisticsService;
import com.arnor4eck.medicinenotes.util.statistics.FullTemplateStatistics;
import com.arnor4eck.medicinenotes.util.statistics.IntakesStatisticsByDate;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/intakes")
    public IntakesStatisticsByDate getIntakeStatisticsByDate(@RequestParam
                                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                             @AuthenticationPrincipal String email){
        return statisticsService.getIntakeStatisticsByDateAndEmail(date, email);
    }

    @GetMapping("/templates/{id}")
    public FullTemplateStatistics getFullTemplateStatistics(@PathVariable long id,
                                                            @RequestParam @Min(0) int offset,
                                                            @AuthenticationPrincipal String email){
        return statisticsService.getFullTemplateStatisticsByIdAndEmail(id, offset, email);
    }
}
