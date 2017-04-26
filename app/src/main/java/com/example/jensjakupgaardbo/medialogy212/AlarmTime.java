package com.example.jensjakupgaardbo.medialogy212;

import java.io.Serializable;
import java.util.Random;

public class AlarmTime implements Serializable{

    String timeID;
    String wakeUp;
    int duration;
    boolean[] days = new boolean[7];

    public AlarmTime(String wakeUp, int duration, boolean[] days) {
        this.timeID = generateID();
        this.wakeUp = wakeUp;
        this.duration = duration;
        this.days = days;
    }

    private String generateID(){
        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public String getTimeID() {
        return timeID;
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

    public String getBedTime() {
        String[] timeParts = wakeUp.split(":");
        int hour = Integer.parseInt( timeParts[0]);

        hour -= duration;
        if(hour < 0){
            hour = 24 + hour;
            return  Integer.toString(hour)+ ":" + timeParts[1];
        }

        return  "0" + Integer.toString(hour)+ ":" + timeParts[1];

    }
}
