package com.example.jensjakupgaardbo.medialogy212;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

public class AlarmLocationService extends Service{

    private static final String TAG = "LOCATIONLISTENER";
    private LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 100f;

    private class AlarmLocationListener implements android.location.LocationListener{

        Location lastLocation;

        public AlarmLocationListener(String provider){
            lastLocation = new Location(provider);
            Log.e(TAG, "LocationListener Constructed" + provider);
        }

        public void saveToPrefs(Location location){
            Gson gson = new GsonBuilder().create();
            String locationString = gson.toJson(new LatLng(location.getLatitude(), location.getLongitude()));
            SharedPreferences getPrefs = getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor e = getPrefs.edit();
            e.putString("lastLocation", locationString);
            e.apply();
        }

        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(AlarmLocationService.this, "Location has changed: "+location.getLatitude()+", "+location.getLongitude(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onLocationChanged: " + location);
            lastLocation.set(location);
            saveToPrefs(lastLocation);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }

    }

    AlarmLocationListener[] locationListeners = new AlarmLocationListener[] {
            new AlarmLocationListener(LocationManager.GPS_PROVIDER),
            new AlarmLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (locationManager != null) {
            for (int i = 0; i < locationListeners.length; i++) {
                try {
                    locationManager.removeUpdates(locationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

}
