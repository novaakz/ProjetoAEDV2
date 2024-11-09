
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import dataStructures.*;
import exceptions.*;
import System.*;


/**
* @author GUILHERME ROCHA (69112) gm.rocha@campus.fct.unl.pt
* @author PÂMELA CUNA (63560) p.cuna@campus.fct.unl.pt
*/

public class Main {

    private static final String DATA_FILE = "storedsystem.dat";

    // Commands
    private static final String INSERT_LINE = "IL";
    private static final String REMOVE_LINE = "RL";
    private static final String CONSULT_LINE = "CL";
    private static final String INSERT_SCHEDULE = "IH";
    private static final String REMOVE_SCHEDULE = "RH";
    private static final String CONSULT_SCHEDULES = "CH";
    private static final String BEST_SCHEDULE = "MH";
    private static final String TERMINATE_APP = "TA";

    // Output messages for SUCCESSFUL operations
    private static final String LINE_INSERT_SUCCESS = "Inserção de linha com sucesso.";
    private static final String LINE_REMOVE_SUCCESS = "Remoção de linha com sucesso.";
    private static final String SCHEDULE_INSERT_SUCCESS = "Criação de horário com sucesso.";
    private static final String SCHEDULE_REMOVE_SUCCESS = "Remoção de horário com sucesso.";
    private static final String APP_TERMINATED_SUCCESS = "Aplicação terminada.";

    // Output messages for FAILED operations
    private static final String EXISTING_LINE = "Linha existente.";
    private static final String NONEXISTING_LINE = "Linha inexistente.";
    private static final String INVALID_SCHEDULE = "Horário inválido.";
    private static final String NONEXISTING_SCHEDULE = "Horário inexistente.";
    private static final String NONEXISTING_DEPART_STATION = "Estação de partida inexistente.";
    private static final String IMPOSSIBLE_ROUTE = "Percurso impossível.";

    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);
        RailwaySystem sys = load();
        String comm;

        do {
            comm = in.next();
            switch(comm.toUpperCase()) {
                case INSERT_LINE -> inLine(in, sys);
                case REMOVE_LINE -> rmLine(in, sys);
                case CONSULT_LINE -> consLine(in, sys);
                case INSERT_SCHEDULE -> inSched(in, sys);
                case REMOVE_SCHEDULE -> rmSched(in, sys);
                case CONSULT_SCHEDULES -> consSched(in, sys);
                case BEST_SCHEDULE -> bestSched(in, sys);
                case TERMINATE_APP -> terminate(sys);
            }
        }while (!comm.toUpperCase().equals(TERMINATE_APP));
        in.close();
        save(sys);
    }

    private static void inLine(Scanner in, RailwaySystem sys) {
        try {
            
            String name = in.nextLine().trim();
            DoubleList<String> stations = makeList(in);
            sys.insertLine(name, stations);
            System.out.println(LINE_INSERT_SUCCESS);

            } catch (ExistentLineException e) {
                System.out.println(EXISTING_LINE);
            }
    }

    private static void rmLine(Scanner in, RailwaySystem sys) {
        try {

            String name = in.nextLine().trim();
            sys.removeLine(name);
            System.out.println(LINE_REMOVE_SUCCESS);

            } catch (InexistentLineExeption e) {
                System.out.println(NONEXISTING_LINE);
            }
    }

    private static void consLine(Scanner in, RailwaySystem sys) {
       try {

            String name = in.nextLine().trim();
            Iterator<Station> it = sys.getLineStations(name);
            while(it.hasNext())
                System.out.println(it.next().getName());

            } catch (InexistentLineExeption e) {
                System.out.println(NONEXISTING_LINE);
            }
    }

    private static void inSched(Scanner in, RailwaySystem sys) {
        try {

            String name = in.nextLine().trim();
            String train = in.nextLine().trim();
            DoubleList<String[]> schedule = new DoubleList<String[]>();
            String[] endLine = getStationTime(in);
            int i = 0;
            while(!endLine[0].isBlank()) {
                schedule.add(i++, endLine);
                endLine = getStationTime(in);
            }

            sys.insertSched(name, train, schedule);
            System.out.println(SCHEDULE_INSERT_SUCCESS);
            
            } catch (InexistentLineExeption e) {
                System.out.println(NONEXISTING_LINE);
            }
            catch (InvalidScheduleException e) {
                System.out.println(INVALID_SCHEDULE);
            }
    }

    private static void rmSched(Scanner in, RailwaySystem sys) {
        try {

            String name = in.nextLine().trim();
            String[] stationTime = getStationTime(in);

            sys.removeSchedule(name, stationTime);

            System.out.println(SCHEDULE_REMOVE_SUCCESS);

            } catch (InexistentLineExeption e) {
                System.out.println(NONEXISTING_LINE);
            } catch (NonexistentScheduleException e) {
                System.out.println(NONEXISTING_SCHEDULE);
            }
    }

    private static void consSched(Scanner in, RailwaySystem sys) {
        try {

            String lineName = in.nextLine().trim();
            String stationName = in.nextLine().trim();

            Iterator<Entry<Time, Schedule>> it = sys.consultSchedules(lineName, stationName);
            while(it.hasNext()) {;
                Schedule schedule = it.next().getValue();
                Iterator<Station> it2 = schedule.getStationIt();
                System.out.println(schedule.getTrain());

                while(it2.hasNext()) {
                    Station station = it2.next();
                    System.out.println(station.getName() + " " + schedule.getStationTime(station).getTime());
                }
            }
            } catch (InexistentLineExeption e) {
                System.out.println(NONEXISTING_LINE);
            } catch (NonexistentStationException e) {
                System.out.println(NONEXISTING_DEPART_STATION);
            }
    }

    private static void bestSched(Scanner in, RailwaySystem sys) {
        try {
            String lineName = in.nextLine().trim();
            String departureName = in.nextLine().trim();
            String destinationName = in.nextLine().trim();
            String timeOfArrival = in.nextLine().trim();

            Schedule schedule = sys.bestSchedule(lineName, departureName, destinationName, timeOfArrival);
            
            Iterator<Station> it = schedule.getStationIt();
            System.out.println(schedule.getTrain());

                while(it.hasNext()) {
                    Station station = it.next();
                    System.out.println(station.getName() + " " + schedule.getStationTime(station).getTime());
                }

            } catch (InexistentLineExeption e) {
                System.out.println(NONEXISTING_LINE);
            } catch (NonexistentStationException e) {
                System.out.println(NONEXISTING_DEPART_STATION);
            } catch (ImpossibleRouteException e) {
                System.out.println(IMPOSSIBLE_ROUTE);
        }
    }

    private static void terminate(RailwaySystem sys) {
        System.out.println(APP_TERMINATED_SUCCESS);
    }

    private static String[] getStationTime(Scanner in) {
        String[] input;
        input = in.nextLine().split(" ");
        return input;
    }

    private static DoubleList<String> makeList(Scanner in) {
        DoubleList<String> stations = new DoubleList<String>();
        String station;

        do{
            station = in.nextLine().trim();
            if(!station.isBlank())
                stations.addLast(station);
        }while(!station.isBlank());

        return stations;
    }

    private static void save(RailwaySystem sys) {
		try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE));
            oos.writeObject(sys);
            oos.flush();
            oos.close();
            }
            catch (IOException e) {}
	}

	private static RailwaySystem load() {
		try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE));
            RailwaySystem sys = (RailwaySystem) ois.readObject();
            ois.close();
            return sys;
            }
            catch (IOException e) {
            // Ficheiro não existe: Criar um objeto vazio
                return new RailwaySystemClass();
            }
            catch (ClassNotFoundException e) {
                return new RailwaySystemClass(); 
        }
	}
}