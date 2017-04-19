package com.example.jensjakupgaardbo.medialogy212;

// class for use in the database of the app, this is the base class that tells us what to save about the alarms

public class Alarm {

    //these are the attributes we want to save in each alarm
    private int _id;
    private String _alarmname;

    public Alarm() {

    }

    public Alarm(String alarmname) {
        this._alarmname = alarmname;
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


}
