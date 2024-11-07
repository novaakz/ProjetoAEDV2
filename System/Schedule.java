package System;

import java.io.Serializable;

public interface Schedule extends Comparable<Schedule>, Serializable {

    void addSched(Station station, Time time);
    
}
