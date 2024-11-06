package System;

import java.io.Serializable;

import dataStructures.DoubleList;
import dataStructures.Iterator;
import exceptions.ExistentLineException;
import exceptions.InexistentLineExeption;

public interface RailwaySystem extends Serializable {

    public void insertLine(String name, DoubleList<String> stationNames) throws ExistentLineException;

    public void removeLine(String name) throws InexistentLineExeption;

    public Iterator<Station> getLineStations(String name) throws InexistentLineExeption;
    
}
