package System;

import dataStructures.*;

public class LineClass implements Line {

    static final long serialVersionUID = 0L;

    protected String name;
    protected OrderedDoubleList<String, Schedule> schedules;
    protected DoubleList<Station> stations;

    public LineClass(String name, DoubleList<Station> stations) {
        this.name = name;
        this.stations = stations;
        this.schedules = new OrderedDoubleList<String, Schedule>();
    }

    @Override
    public String getName() {
        return name;
    }

    public void insertSchedule(String train, Schedule schedule) {
        schedules.insert(train, schedule);
    }

    @Override
    public void removeSchedule(String train) {
        schedules.remove(train);
    }

    public DoubleList<Station> getStations() {
        return this.stations;
    }

    public Iterator<Station> getStationsIt() {
        return stations.iterator();
    }

    public Iterator<Entry<String, Schedule>> getScheduleIt() {
        return schedules.iterator();
    }

    public boolean isDepartingStation(Station station) {
        return stations.getFirst().equals(station) || stations.getLast().equals(station);
    }

    @Override
    public int compareTo(Line o) {
        return this.name.toUpperCase().compareTo(o.getName().toUpperCase());
    }

}
