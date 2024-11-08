package System;

public class TimeClass implements Time {

    static final long serialVersionUID = 0L;

    private String hour;
    private String minute;

    public TimeClass (String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getTime() {
        return hour + ":" + minute;
    }

    public boolean hasTravelTime(Time other) {
        return this.compareTo(other) < 0;
    }

    @Override
    public int compareTo(Time o) {
        int intHour = Integer.parseInt(hour);
        int intMin = Integer.parseInt(minute);
        
        if(intHour - Integer.parseInt(o.getHour()) == 0)
            return intMin - Integer.parseInt(o.getMinute());
        return intHour - Integer.parseInt(o.getHour());
    }

}
