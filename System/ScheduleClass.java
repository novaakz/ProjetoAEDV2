package System;

import dataStructures.OrderedDoubleList;

public class ScheduleClass implements Schedule {

    static final long serialVersionUID = 0L;

    private OrderedDoubleList<String, Time> schedule;

    public ScheduleClass() {
        this.schedule = new OrderedDoubleList<String, Time>();
    }
    
    @Override
    public int compareTo(Schedule o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
    
}
