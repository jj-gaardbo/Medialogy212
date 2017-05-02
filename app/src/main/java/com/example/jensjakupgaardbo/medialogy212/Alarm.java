package com.example.jensjakupgaardbo.medialogy212;

// class for use in the database of the app, this is the base class that tells us what to save about the alarms
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
public class Alarm implements Serializable{

    //these are the attributes we want to save in each alarm
    private int _id;
    private boolean active = false;
    private String _alarmname;
    private ArrayList<AlarmTime> alarmTimes = new ArrayList<>();
    private LatLng _latlng;
    boolean hasFullWeek = false;

    public Alarm() {

    }

    public Alarm(String alarmname, LatLng location) {
        this._alarmname = alarmname;
        this._latlng = location;

    }

    public String getWakeTimeOfDay(int dayOfWeek){
        String alarmTime = "set for today";
        for(AlarmTime t : alarmTimes){
            if(t.days[dayOfWeek]){
                alarmTime = t.getWakeUp();
                break;
            }
        }
        return alarmTime;

    }
    //getters and setters, use these to get or modify the data in the database
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_alarmname() {        return _alarmname;
    }

    public void set_alarmName(String _alarmname) {
        this._alarmname = _alarmname;
    }

    public ArrayList<AlarmTime> getAlarmTimes() {
        return alarmTimes;
    }

    public void setAlarmTimes(ArrayList<AlarmTime> alarmTimes) {
        this.alarmTimes = alarmTimes;
    }

    public LatLng get_latlng() {
        return _latlng;
    }

    public void set_latlng(LatLng _latlng) {
        this._latlng = _latlng;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getBedTime(int dayOfWeek) {
        String bedtime = "No alarm";
        for(AlarmTime t : alarmTimes){
            if(t.days[dayOfWeek]){
                bedtime = t.getBedTime();
                break;
            }

        }


        return bedtime;
    }
    public int getSleepDuration(int dayOfWeek){
        int sleepDur = 0;
        for(AlarmTime t : alarmTimes){
            if(t.days[dayOfWeek]){
                sleepDur = t.getDuration();
                break;
            }
        }
        return sleepDur;
    }

    public static LatLng reconvertLocation(Alarm alarm, double[] alarmLocationConverted){
        return new LatLng(alarmLocationConverted[0], alarmLocationConverted[1]);
    }

    public static double[] getConvertedLocation(LatLng location){
        double[] locationArray = new double[2];
        locationArray[0] = location.latitude;
        locationArray[1] = location.longitude;
        return locationArray;
    }

}
