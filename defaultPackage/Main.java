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
    private static final String TIMETABLE_INSERT_SUCCESS = "Criação de horário com sucesso.";
    private static final String TIMETABLE_REMOVE_SUCCESS = "Remoção de horário com sucesso.";
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
                case CONSULT_SCHEDULES -> System.out.println("IN DEV");
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

    private static final void rmLine(Scanner in, RailwaySystemClass sys) {
        try {
            String name = in.nextLine().trim();
            sys.removeLine(name);
            System.out.println(LINE_REMOVE_SUCCESS);
            } catch (InexistentLineExeption e) {
                System.out.println(NONEXISTING_LINE);
            }
    }

    private static final void consLine(Scanner in, RailwaySystemClass sys) {
        try {
            String name = in.nextLine().trim();
            Iterator<Station> it = sys.getLineStations(name);
            while(it.hasNext())
                System.out.println(it.next().getName());
            } catch (InexistentLineExeption e) {
                System.out.println(NONEXISTING_LINE);
            }
    }

    private static final void inSched(Scanner in, RailwaySystemClass sys) {
        try {
            String name = in.nextLine().trim();
            String train = in.nextLine().trim();
            DoubleList<String[]> schedule = new DoubleList<String[]>();
            String[] stationTime;
            int i = 0;
            
            do {
                schedule.add(i, getStationTime(in));
                stationTime = schedule.get(i++);
            } while(stationTime != null);

            sys.insertSched(name, train, schedule);
            
        } catch (InexistentLineExeption e) {
                System.out.println(NONEXISTING_LINE);
            }
        catch (InvalidScheduleException e) {
            System.out.println(INVALID_SCHEDULE);
        }
    }

    private static final void terminate(RailwaySystemClass sys) {
        save(sys);
        System.out.println(APP_TERMINATED_SUCCESS);
    }

    private static final String[] getStationTime(Scanner in) {
        String[] input;
        input = in.nextLine().split(" ");
        return input;
    }

    private static final DoubleList<String> makeList(Scanner in) {
        DoubleList<String> stations = new DoubleList<String>();
        String station = in.nextLine();
        int i = 0;
        while(!station.isEmpty()) {
            stations.add(i++, station);
            station = in.nextLine();
        }
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