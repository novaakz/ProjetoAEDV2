package System;

public class TimeClass implements Time {

    static final long serialVersionUID = 0L;

    private int hour;
    private int minute;

    public TimeClass (int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean hasTravelTime(Time other) {
        if(this.hour == other.getHour())
            return this.minute < other.getMinute();
        return this.hour < other.getHour();
    }

}
