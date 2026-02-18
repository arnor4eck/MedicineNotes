package com.arnor4eck.medicinenotes.util.statistics;

import com.arnor4eck.medicinenotes.entity.IntakesStatus;

import java.util.Arrays;

public class IntakesStatisticsByDateUnit {
    private final String name;
    private StatusAndCountUnit[] units;

    public IntakesStatisticsByDateUnit(String name){
        this.name = name;
        this.units = (StatusAndCountUnit[]) Arrays.stream(IntakesStatus.values())
                .map(s -> new StatusAndCountUnit(s, 0)).toArray(StatusAndCountUnit[]::new);;

    }

    public void setCountForStatus(IntakesStatus status,
                                  long count){
        for(StatusAndCountUnit statusAndCountUnit : this.units)
            if(statusAndCountUnit.getStatus() == status) {
                statusAndCountUnit.setCount(count);
                return;
            }

    }
}
