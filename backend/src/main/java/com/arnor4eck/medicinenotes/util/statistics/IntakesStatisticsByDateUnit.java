package com.arnor4eck.medicinenotes.util.statistics;

import com.arnor4eck.medicinenotes.entity.IntakesStatus;
import lombok.Getter;

@Getter
public class IntakesStatisticsByDateUnit {
    private StatusAndCountUnit[] units;

    public IntakesStatisticsByDateUnit(){
        this.units = new StatusAndCountUnit[IntakesStatus.values().length];

        for(IntakesStatus status : IntakesStatus.values())
            units[status.ordinal()] = new StatusAndCountUnit(status, 0);
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
