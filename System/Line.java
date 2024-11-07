package System;

import java.io.Serializable;

import dataStructures.*;

public interface Line extends Comparable<Line>, Serializable {

    String getName();

    void insertSchedule(String train, Schedule schedule);

    DoubleList<Station> getStations();

    Iterator<Station> getStationsIt();

}
