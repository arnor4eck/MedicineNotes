package com.arnor4eck.medicinenotes.util.statistics;

import com.arnor4eck.medicinenotes.entity.IntakesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StatusAndCountUnit {
    private final IntakesStatus status;

    private long count;

}
