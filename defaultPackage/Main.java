package defaultPackage;

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
    private static final String NONEXISTING_STATION = "Estação inexistente.";
    private static final String INVALID_SCHEDULE = "Horário inválido.";
    private static final String NONEXISTING_DEPART_STATION = "Estação de partida inexistente.";
    private static final String IMPOSSIBLE_ROUTE = "Percurso impossível.";

    public static void main(String args[]) {
        
        Scanner in = new Scanner(System.in);
        String comm;
        RailwaySystemClass sys = load();

        do {
            comm = in.next();
            switch(comm.toUpperCase()) {
                case INSERT_LINE -> inLine(in, sys);
                case REMOVE_LINE -> rmLine(in, sys);
                case CONSULT_LINE -> consLine(in, sys);
                case INSERT_SCHEDULE -> inSched(in, sys);
                case REMOVE_SCHEDULE -> System.out.println("IN DEV");
                case CONSULT_SCHEDULES -> consSched(in, sys);
                case BEST_SCHEDULE -> System.out.println("IN DEV");
                case TERMINATE_APP -> terminate(sys);
            }
        }while (!comm.equals(TERMINATE_APP));
        in.close();
    }

    private static void inLine(Scanner in, RailwaySystemClass sys) {
        try {
            String name = in.nextLine().trim();
            DoubleList<String> stations = makeList(in);
            sys.insertLine(name, stations);
            System.out.println(LINE_INSERT_SUCCESS);

            } catch (ExistentLineException e) {
            System.out.println(EXISTING_LINE);
            }
    }

    private static void rmLine(Scanner in, RailwaySystemClass sys) {
        try {
            String name = in.nextLine().trim();
            sys.removeLine(name);
            System.out.println(LINE_REMOVE_SUCCESS);

            } catch (InexistentLineExeption e) {
                System.out.println(NONEXISTING_LINE);
            }
    }

    private static void consLine(Scanner in, RailwaySystemClass sys) {
        try {
            String name = in.nextLine().trim();
            Iterator<Station> it = sys.getLineStations(name);
            while(it.hasNext())
                System.out.println(it.next().getName());

            } catch (InexistentLineExeption e) {
                System.out.println(NONEXISTING_LINE);
            }
    }

    private static void inSched(Scanner in, RailwaySystemClass sys) {
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
            System.out.println(INVALID_SCHEDULE + " " + e.getMessage());
        }
    }

    private static void consSched(Scanner in, RailwaySystemClass sys) {
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

    private static void terminate(RailwaySystemClass sys) {
        save(sys);
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

    private static void save(RailwaySystemClass sys) {
		try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE));
            oos.writeObject(sys);
            oos.flush();
            oos.close();
            }
            catch (IOException e) {}
	}

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static RailwaySystemClass load() {
		try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE));
            RailwaySystemClass sys = (RailwaySystemClass) ois.readObject();
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