package System;

import dataStructures.*;

public class LineClass implements Line {

    static final long serialVersionUID = 0L;

    protected OrderedDoubleList<String, DoubleList<Station>> schedules;
    protected String name;

    public LineClass(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Line o) {
        return this.name.toUpperCase().compareTo(o.getName().toUpperCase());
    }

}
