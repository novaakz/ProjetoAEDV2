package System;

import dataStructures.*;

public class ScheduleClass implements Schedule {

    static final long serialVersionUID = 0L;

    protected OrderedDoubleList<Station, Time> schedule;
    protected DoubleList<Station> orderedStations;
    protected String train;

    public ScheduleClass() {
        this.schedule = new OrderedDoubleList<Station, Time>();
        this.orderedStations = new DoubleList<Station>();
    }

    public void addTrain(String train) {
        this.train = train;
    }

    public void addSched(Station station, Time time) {
        orderedStations.addLast(station);
        schedule.insert(orderedStations.getLast(), time);
    }

    public Station getDepartureStation() {
        return orderedStations.getFirst();
    }

    public Time getDepartureTime() {
        return schedule.find(getDepartureStation());
    }

    public String getTrain() {
        return train;
    }
    
    public Iterator<Station> getStationIt() {
        return orderedStations.iterator();
    }

    public Time getStationTime(Station station) {
        return schedule.find(station);
    }

    public boolean existsStation(Station station) {
        return orderedStations.find(station) != -1;
    }
    
    @Override
    public int compareTo(Schedule o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
    
}
