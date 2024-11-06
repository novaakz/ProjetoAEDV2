package System;

import dataStructures.*;

public class StationClass implements Station {

    static final long serialVersionUID = 0L;

    protected OrderedDoubleList<Line, OrderedDoubleList<String, Time>> lines;
    protected String name;

    public StationClass(String name) {
        this.lines = new OrderedDoubleList<Line, OrderedDoubleList<String, Time>>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addLine(Line line) {
        lines.insert(line, new OrderedDoubleList<String, Time>());
    }

    public void removeLine(Line line) {
        lines.remove(line);
    }

    public boolean isAbandoned() {
        return lines.isEmpty();
    }

    @Override
    public int compareTo(Station o) {
        return this.name.toUpperCase().compareTo(o.getName().toUpperCase());
    }

}