package System;

import java.io.Serializable;

import dataStructures.*;

public interface Schedule extends Comparable<Schedule>, Serializable {

    void addSched(Station station, Time time);

    void addTrain(String train);

    Station getDepartureStation();

    Time getDepartureTime();

    String getTrain();

    Iterator<Station> getStationIt();

    Time getStationTime(Station station);
    
}
