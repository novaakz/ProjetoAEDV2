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

    public boolean hasTravelTime(Time other) {
        int intHour = Integer.parseInt(hour);
        int intMin = Integer.parseInt(minute);
        
        if(intHour == Integer.parseInt(other.getHour()))
            return intMin < Integer.parseInt(other.getMinute());
        return intHour < Integer.parseInt(other.getHour());
    }

}
