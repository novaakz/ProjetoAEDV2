package System;

import java.io.Serializable;

public interface Station extends Comparable<Station>, Serializable {

    String getName();

    void addLine(Line line);

    void removeLine(Line line);

    boolean isAbandoned();
    
}
