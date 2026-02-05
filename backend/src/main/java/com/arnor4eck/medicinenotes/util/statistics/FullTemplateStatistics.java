package com.arnor4eck.medicinenotes.util.statistics;

import java.util.Collection;

public record FullTemplateStatistics(Collection<TemplateStatisticsUnit> templateStatistics,
                                     long maxTimesPerDay){
}
