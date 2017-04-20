package com.example.jensjakupgaardbo.medialogy212;

import java.io.Serializable;

public class Time implements Serializable{

    String wakeUp;
    int duration;
    boolean[] days = new boolean[7];

    public Time(String wakeUp, int duration, boolean[] days) {
        this.wakeUp = wakeUp;
        this.duration = duration;
        this.days = days;
    }

    public String getWakeUp() {
        return wakeUp;
    }

    public int getDuration() {
        return duration;
    }

    public boolean[] getDays() {
        return days;
    }

    public void setWakeUp(String wakeUp) {
        this.wakeUp = wakeUp;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDays(boolean[] days) {
        this.days = days;
    }

}
