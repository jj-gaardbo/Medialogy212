package com.example.jensjakupgaardbo.medialogy212;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

public class AlarmLocationListener implements LocationListener {

    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private boolean canGetLocation;

    public LocationManager locationManager;

    public Location lastLocation;

    public double latitude = 0.0;
    public double longitude = 0.0;

    String TAG = "LOCATIONLISTENER";

    public AlarmLocationListener(Context context, String provider){
        initializeLocationManager(context);
        request(context);
        Log.e(TAG, "LocationListener Constructed" + provider);
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    public void getLocation(Context context) {
        try {
            locationManager = (LocationManager) context
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (lastLocation == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                1000,
                                1, this);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            lastLocation = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (lastLocation != null) {
                                latitude = lastLocation.getLatitude();
                                longitude = lastLocation.getLongitude();
                            }
                        }
                    }
                }
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1000,
                            1, this);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        lastLocation = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (lastLocation != null) {
                            latitude = lastLocation.getLatitude();
                            longitude = lastLocation.getLongitude();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void request(Context context){

        getLocation(context);

    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        longitude = longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: " + location);
        if(location != null){
            setLastLocation(location);
            setLatitude(location.getLatitude());
            setLongitude(location.getLongitude());
        }
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

    private void initializeLocationManager(Context context){
        Log.e(TAG, "initializeLocationManager");
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        }
    }

}
