package com.example.jensjakupgaardbo.medialogy212;

// class for use in the database of the app, this is the base class that tells us what to save about the alarms
import com.google.android.gms.maps.model.LatLng;
public class Alarm {

    //these are the attributes we want to save in each alarm
    private int _id;
    private String _alarmname;
    private LatLng _latlng;


    public Alarm() {

    }

    public Alarm(String alarmname, LatLng location) {
        this._alarmname = alarmname;
        this._latlng = location;
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

    public LatLng get_latlng() {
        return _latlng;
    }

    public void set_latlng(LatLng _latlng) {
        this._latlng = _latlng;
    }
}
