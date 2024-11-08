package System;

import java.io.Serializable;

public interface Time extends Serializable, Comparable<Time> {

    public String getHour();

    public String getMinute();

    boolean hasTravelTime(Time other);

    String getTime();
    
}
