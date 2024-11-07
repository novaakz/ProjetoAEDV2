package System;

import dataStructures.*;
import exceptions.*;

public class RailwaySystemClass implements RailwaySystem {

    static final long serialVersionUID = 0L;

    protected OrderedDoubleList<Line, DoubleList<Station>> lines;
    protected OrderedDoubleList<String, Station> stations;
    
    public RailwaySystemClass() {
        lines = new OrderedDoubleList<Line, DoubleList<Station>>();
        stations = new OrderedDoubleList<String, Station>();
    }

    public void insertLine(String name, DoubleList<String> stationNames) throws ExistentLineException {
        Line line = new LineClass(name);
        if(existsLine(line))
            throw new ExistentLineException();
        DoubleList<Station> stationsList = arrangeStations(stationNames);
        lines.insert(line, stationsList);
        Iterator<Station> it = stationsList.iterator();
        while(it.hasNext())
            it.next().addLine(line);
    }

    public void removeLine(String name) throws InexistentLineExeption {
        Line line = new LineClass(name);
        if(!existsLine(line))
            throw new InexistentLineExeption();
        DoubleList<Station> lineStations = lines.remove(line);
        Iterator<Station> it = lineStations.iterator();
        while(it.hasNext()) {
            Station s = it.next();
            if(s.isAbandoned())
                stations.remove(s.getName());
        }
    }

    public Iterator<Station> getLineStations(String name) throws InexistentLineExeption {
        Line line = new LineClass(name);
        if(!existsLine(line))
            throw new InexistentLineExeption();
        return lines.find(line).iterator();
    }

    public void insertSched(String lineName, String train, DoubleList<String[]> stationTime) 
    throws InexistentLineExeption, InvalidScheduleException {

        Line line = new LineClass(lineName);
        if(!existsLine(line))
            throw new InexistentLineExeption();
        
        Iterator<String[]> it = stationTime.iterator();
        String stationName = arrangeStationName(stationTime.getFirst());
        DoubleList<Station> stations = lines.find(line);
        int stationPos = 0;

        if(!stationName.equals(stations.getFirst().getName())) {
            if(!stationName.equals(stations.getLast().getName()))
                throw new InvalidScheduleException();
            stationPos = stations.size() - 1;
        }

        while(it.hasNext()) {
            String[] tmp = it.next();
            Time time = parseTime(tmp[tmp.length - 1]);
            stationName = arrangeStationName(tmp);
            int i = stationPos;
        }
    }

    private String arrangeStationName(String[] station) {
        String stationName = station[0];
        for(int i = 1; i < station.length - 1; i++)
                stationName += " " + station[i];
        return stationName;
    }

    private Time parseTime(String timeLine) {
        String[] tmp = timeLine.split(":");
        int hour = Integer.parseInt(tmp[0]);
        int min = Integer.parseInt(tmp[1]);
        return new TimeClass(hour, min);
    }

    private boolean existsLine(Line line) {
        if(lines.find(line) != null)
            return true;
        return false;
    }

    private DoubleList<Station> arrangeStations(DoubleList<String> stationsNames) {
        DoubleList<Station> stationsList= new DoubleList<Station>();
        Iterator<String> it = stationsNames.iterator();
        int i = 0;

        while(it.hasNext()) {
            String sName = it.next();
            Station station = this.stations.find(sName);

            // IF station is null, therefore doesn't exist
            if(!(station != null)) {
                station = new StationClass(sName);
                this.stations.insert(sName, station);
            }
            stationsList.add(i++, station);
        }
        return stationsList;
    }
}