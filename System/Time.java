package System;

import java.io.Serializable;

public interface Time extends Serializable {
    public int getHour();

    public int getMinute();

    boolean hasTravelTime(Time other);
}
