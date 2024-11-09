package System;

import java.io.Serializable;

import dataStructures.*;

public interface Line extends Comparable<Line>, Serializable {

    String getName();

    void insertSchedule(String train, Schedule schedule);

    DoubleList<Station> getStations();

    Iterator<Station> getStationsIt();

    Iterator<Entry<String, Schedule>> getScheduleIt();

    boolean isDepartingStation(Station station);

    void removeSchedule(String train);

    boolean existsStation(Station station);

    Schedule getBestSchedule(Station departure, Station destination, Time time);

}
