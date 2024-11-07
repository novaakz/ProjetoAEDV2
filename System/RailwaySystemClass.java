package System;

import dataStructures.*;
import exceptions.*;

public class RailwaySystemClass implements RailwaySystem {

    static final long serialVersionUID = 0L;

    protected OrderedDoubleList<String, Line> lines;
    protected OrderedDoubleList<String, Station> stations;
    
    public RailwaySystemClass() {
        lines = new OrderedDoubleList<String, Line>();
        stations = new OrderedDoubleList<String, Station>();
    }

    public void insertLine(String name, DoubleList<String> stationNames) throws ExistentLineException {
        if(existsLine(name))
            throw new ExistentLineException();
        DoubleList<Station> stationsList = arrangeStations(stationNames);
        Line line = new LineClass(name, stationsList);
        lines.insert(name.toUpperCase(), line);
        Iterator<Station> it = stationsList.iterator();
        while(it.hasNext()) {
            Station station = it.next();
                if(!existsStation(station.getName()))
                    stations.insert(station.getName().toUpperCase(), station);
            station.addLine(line);
        }
    }

    public void removeLine(String name) throws InexistentLineExeption {
        if(!existsLine(name))
            throw new InexistentLineExeption();
        Line line = lines.remove(name.toUpperCase());
        DoubleList<Station> lineStations = line.getStations();
        Iterator<Station> it = lineStations.iterator();
        while(it.hasNext()) {
            Station station = it.next();
            station.removeLine(line);
            if(station.isAbandoned())
                stations.remove(station.getName().toUpperCase());
        }
    }

    public Iterator<Station> getLineStations(String name) throws InexistentLineExeption {
        if(!existsLine(name))
            throw new InexistentLineExeption();
        return lines.find(name.toUpperCase()).getStationsIt();
    }

    public void insertSched(String lineName, String train, DoubleList<String[]> stationTime) 
    throws InexistentLineExeption, InvalidScheduleException {
        
        if(!existsLine(lineName))
            throw new InexistentLineExeption();
        
        Line line = lines.find(lineName.toUpperCase());

        String firstStationName = arrangeStationName(stationTime.getFirst());
        int dir = 1;
        int currentPos = 0;
        DoubleList<Station> lineStations = line.getStations();

        if(!firstStationName.equals(lineStations.getFirst().getName())) {
            if(!firstStationName.equals(lineStations.getLast().getName()))
                throw new InvalidScheduleException();
            else {
                dir = -1;
                currentPos = lineStations.size() - 1;
            }
        }

        DoubleList<Station> scheduleStations = new DoubleList<Station>();
        DoubleList<Time> scheduleTimes = new DoubleList<Time>();
        
        Iterator<String[]> it = stationTime.iterator();
        String[] tmp = it.next();
        scheduleStations.addFirst(this.stations.find(arrangeStationName(tmp)));
        scheduleTimes.addFirst(parseTime(tmp[tmp.length - 1]));

        int stationsValidated = 1;

        while(it.hasNext()) {
            tmp = it.next();
            Station station = this.stations.find(arrangeStationName(tmp));
            Time time = parseTime(tmp[tmp.length - 1]);
            boolean found = false;
            if(dir == 1) {
                while(currentPos < lineStations.size() && !found) {
                    if(lineStations.get(currentPos).equals(station))
                        found = true;
                    else
                        currentPos++;
                }
                if(!found || !scheduleTimes.get(stationsValidated - 1).hasTravelTime(time))
                    throw new InvalidScheduleException();
                scheduleStations.add(stationsValidated, station);
                scheduleTimes.add(stationsValidated++, time);
            }
            else {
                while(currentPos > 0 && !found) {
                    if(lineStations.get(currentPos).equals(station))
                        found = true;
                    else
                        currentPos--;
                }
                if(!found || !scheduleTimes.get(stationsValidated - 1).hasTravelTime(time))
                    throw new InvalidScheduleException();
                scheduleStations.add(stationsValidated, station);
                scheduleTimes.add(stationsValidated++, time);
            }
        }

        Schedule schedule = new ScheduleClass();
        Iterator<Station> s = scheduleStations.iterator();
        Iterator<Time> t = scheduleTimes.iterator();
        
        while(s.hasNext())
            schedule.addSched(s.next(), t.next());

        line.insertSchedule(train, schedule);
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

    private boolean existsLine(String lineName) {
        if(lines.find(lineName.toUpperCase()) != null)
            return true;
        return false;
    }

    private boolean existsStation(String stationName) {
        if(stations.find(stationName.toUpperCase()) != null)
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