package System;

import dataStructures.OrderedDoubleList;

public class ScheduleClass implements Schedule {

    static final long serialVersionUID = 0L;

    private OrderedDoubleList<Station, Time> schedule;

    public ScheduleClass() {
        this.schedule = new OrderedDoubleList<Station, Time>();
    }

    public void addSched(Station station, Time time) {
        schedule.insert(station, time);
    }
    
    @Override
    public int compareTo(Schedule o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
    
}
