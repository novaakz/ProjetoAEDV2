package System;

import java.io.Serializable;

import dataStructures.*;
import exceptions.*;

public interface RailwaySystem extends Serializable {

    void insertLine(String name, DoubleList<String> stationNames) throws ExistentLineException;

    void removeLine(String name) throws InexistentLineExeption;

    Iterator<Station> getLineStations(String name) throws InexistentLineExeption;
    
    void insertSched(String lineName, String train, DoubleList<String[]> stationTime) 
    throws InexistentLineExeption, InvalidScheduleException;

    void removeSchedule(String lineName, String[] stationAndTime) 
    throws InexistentLineExeption, NonexistentScheduleException;

    Iterator<Entry<Time, Schedule>> consultSchedules(String lineName, String stationName) 
    throws InexistentLineExeption, NonexistentStationException;

    Schedule bestSchedule(String lineName, String departure, String destination, String timeOfArrival) 
    throws InexistentLineExeption, NonexistentStationException, ImpossibleRouteException;
}
