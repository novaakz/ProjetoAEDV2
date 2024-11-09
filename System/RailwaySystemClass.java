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
        if(existsLine(name.toUpperCase()) != null)
            throw new ExistentLineException();

        DoubleList<Station> stationsList = arrangeStations(stationNames);

        Line line = new LineClass(name, stationsList);
        lines.insert(name.toUpperCase(), line);
        Iterator<Station> it = stationsList.iterator();

        while(it.hasNext())
            it.next().addLine(line);
    }

    public void removeLine(String name) throws InexistentLineExeption {

        if(existsLine(name.toUpperCase()) == null)
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
        if(existsLine(name.toUpperCase()) == null)
            throw new InexistentLineExeption();

        return lines.find(name.toUpperCase()).getStationsIt();
    }

    public void insertSched(String lineName, String train, DoubleList<String[]> stationTime) 
    throws InexistentLineExeption, InvalidScheduleException {
        
        if(existsLine(lineName.toUpperCase()) == null)
            throw new InexistentLineExeption();
        
        Line line = lines.find(lineName.toUpperCase());

        String firstStationName = arrangeStationName(stationTime.getFirst());
        int dir = 1;
        int currentPos = 0;
        DoubleList<Station> lineStations = line.getStations();

        if(!firstStationName.toUpperCase().equals(lineStations.getFirst().getName().toUpperCase())) {

            if(!firstStationName.toUpperCase().equals(lineStations.getLast().getName().toUpperCase()))
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
        scheduleStations.addFirst(this.stations.find(arrangeStationName(tmp).toUpperCase()));
        scheduleTimes.addFirst(parseTime(tmp[tmp.length - 1]));

        int stationsValidated = 1;

        while(it.hasNext()) {
            tmp = it.next();
            Station station = this.stations.find(arrangeStationName(tmp).toUpperCase());
            Time time = parseTime(tmp[tmp.length - 1]);
            boolean found = false;

            if(dir == 1) {

                while(currentPos < lineStations.size() && !found) {
                    if(lineStations.get(currentPos).getName().equals(station.getName()))
                        found = true;
                    else
                        currentPos++;
                }
            }
            else {

                while(currentPos >= 0 && !found) {
                    if(lineStations.get(currentPos).getName().equals(station.getName()))
                        found = true;
                    else
                        currentPos--;
                }
            }

            if(!found)
                    throw new InvalidScheduleException();

            if(!scheduleTimes.get(stationsValidated - 1).hasTravelTime(time))
                throw new InvalidScheduleException();

            scheduleStations.add(stationsValidated, station);
            scheduleTimes.add(stationsValidated, time);
            stationsValidated++;
        }

        Schedule schedule = new ScheduleClass();
        schedule.addTrain(train);
        int counter = 0;
        
        while(counter < stationsValidated) {
            schedule.addSched(scheduleStations.get(counter), scheduleTimes.get(counter));
            counter++;
        }

        line.insertSchedule(train, schedule);
    }

    public void removeSchedule(String lineName, String[] stationAndTime) 
    throws InexistentLineExeption, NonexistentScheduleException {
        if(existsLine(lineName.toUpperCase()) == null)
            throw new InexistentLineExeption();

        Line line = lines.find(lineName.toUpperCase());

        String stationName = arrangeStationName(stationAndTime);
        Time time = parseTime(stationAndTime[stationAndTime.length - 1]);

        Iterator<Entry<String, Schedule>> schedules = line.getScheduleIt();
        boolean found = false;

        while (schedules.hasNext() && !found) {
            Schedule schedule = schedules.next().getValue();

            if(schedule.getDepartureStation().equals(stations.find(stationName.toUpperCase()))) {

                if(schedule.getDepartureTime().compareTo(time) == 0) {
                    line.removeSchedule(schedule.getTrain());
                    found = true;
                }
            }
        }
        if (!found) {
            throw new NonexistentScheduleException();
        }
    }

    public Iterator<Entry<Time, Schedule>> consultSchedules(String lineName, String stationName) 
    throws InexistentLineExeption, NonexistentStationException {

        if(existsLine(lineName.toUpperCase()) == null)
            throw new InexistentLineExeption();
        if(!existsStation(stationName))
            throw new NonexistentStationException();

        Line line = lines.find(lineName.toUpperCase());
        Station station = stations.find(stationName.toUpperCase());

        if(!line.isDepartingStation(station))
            throw new NonexistentStationException();

        Iterator<Entry<String, Schedule>> it = line.getScheduleIt();
        OrderedDoubleList<Time, Schedule> schedules = new OrderedDoubleList<Time, Schedule>();

        while(it.hasNext()) {
            Entry<String, Schedule> entry = it.next();
            if(entry.getValue().getDepartureStation().equals(station)) {
                schedules.insert(entry.getValue().getDepartureTime(), entry.getValue());
            }
        }

        return schedules.iterator();
    }

    public Schedule bestSchedule(String lineName, String departure, String destination, String timeOfArrival) 
    throws InexistentLineExeption, NonexistentStationException, ImpossibleRouteException {
        
        if(existsLine(lineName.toUpperCase()) == null)
            throw new InexistentLineExeption();

        Line line = lines.find(lineName.toUpperCase());
        Station depart = stations.find(departure.toUpperCase());

        if(!line.existsStation(depart))
            throw new NonexistentStationException();

        Station destin = stations.find(destination.toUpperCase());
        if(!line.existsStation(destin))
            throw new ImpossibleRouteException();
        
            Time time = parseTime(timeOfArrival);
        Schedule bestSchedule = line.getBestSchedule(depart, destin, time);
        
        if(bestSchedule == null)
            throw new ImpossibleRouteException();

        return bestSchedule;
    }

    private String arrangeStationName(String[] station) {
        String stationName = station[0];
        for(int i = 1; i < station.length - 1; i++)
                stationName += " " + station[i];
        return stationName;
    }

    private Time parseTime(String timeLine) {
        String[] tmp = timeLine.split(":");
        return new TimeClass(tmp[0], tmp[1]);
    }

    private Line existsLine(String lineName) {
        return lines.find(lineName.toUpperCase());
    }

    private boolean existsStation(String stationName) {
        if(stations.find(stationName.toUpperCase()) != null)
            return true;
        return false;
    }

    private DoubleList<Station> arrangeStations(DoubleList<String> stationsNames) {
        DoubleList<Station> stationsList = new DoubleList<Station>();
        Iterator<String> it = stationsNames.iterator();

        while(it.hasNext()) {
            String sName = it.next();
            Station station = this.stations.find(sName.toUpperCase());

            // IF station is null, therefore doesn't exist
            if(station == null) {
                station = new StationClass(sName);
                this.stations.insert(sName.toUpperCase(), station);
            }
            stationsList.addLast(station);
        }
        return stationsList;
    }
}