package com.arnor4eck.medicinenotes.util.statistics;

import com.arnor4eck.medicinenotes.entity.IntakesStatus;

import java.util.Arrays;

public class IntakesStatisticsByDateUnit {
    private final String name;
    private StatusAndCountUnit[] units;

    public IntakesStatisticsByDateUnit(String name){
        this.name = name;
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

    @Override
    public int hashCode(){
        return 31 * this.name.hashCode() + Arrays.hashCode(units);
    }
    @Override
    public boolean equals(Object o){
        if(o instanceof IntakesStatisticsByDateUnit) {
            IntakesStatisticsByDateUnit other = (IntakesStatisticsByDateUnit) o;

            return this.name.equals(other.name) && Arrays.equals(this.units, other.units);
        }

        return false;
    }
}
