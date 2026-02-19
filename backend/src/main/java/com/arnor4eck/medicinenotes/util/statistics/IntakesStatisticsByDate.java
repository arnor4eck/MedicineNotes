package com.arnor4eck.medicinenotes.util.statistics;

import java.util.Map;

public record IntakesStatisticsByDate(Map<String, IntakesStatisticsByDateUnit> statisticsByDate) {}
