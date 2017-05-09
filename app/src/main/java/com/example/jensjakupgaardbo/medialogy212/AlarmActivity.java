package com.example.jensjakupgaardbo.medialogy212;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class AlarmActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationButtonClickListener {

    private static final String TAG = AlarmActivity.class.getSimpleName();

    public GoogleMap googleMap;
    boolean editing;
    String oldAlarmName = "";

    Alarm alarm;
    EditText nameInput;
    TimeAdapter adapter;
    FloatingActionButton addTime;

    double[] alarmLocationConverted;
    private final static int PLACE_PICKER_REQUEST = 1;
    private boolean locationPerm = false;

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        updateLocationUI();
        setUpMap();
    }

    public void setUpMap() {
            googleMap.clear();
            getLocation();
            googleMap.setMinZoomPreference(10);
            googleMap.setMaxZoomPreference(30);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style_json));
            googleMap.setOnMarkerDragListener(this);
            googleMap.setOnMapClickListener(this);
            googleMap.setOnMyLocationButtonClickListener(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment_alarm);
        mapFragment.getMapAsync(this);

        editing = getIntent().getBooleanExtra("editing", false);
        alarm = (getIntent().getSerializableExtra("activeAlarm") != null) ? (Alarm) getIntent().getSerializableExtra("activeAlarm") : new Alarm();

        alarmLocationConverted = (alarmLocationConverted == null) ? getIntent().getDoubleArrayExtra("activeAlarmLocation") : null;
        if(alarmLocationConverted != null){
            alarm.set_latlng(Alarm.reconvertLocation(alarm, alarmLocationConverted));
        }

        if (getIntent().getStringExtra("editing_name") != null) {
            oldAlarmName = getIntent().getStringExtra("editing_name");
        } else if (editing) {
            oldAlarmName = alarm.get_alarmname();
        }
        addTime = (FloatingActionButton) findViewById(R.id.add_alarm);
        nameInput = (EditText) findViewById(R.id.alarm_name);

        if (alarm.get_alarmname() != null) {
            nameInput.setText(alarm.get_alarmname());
        }

        nameInput.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                nameInput.setCursorVisible(true);
                addTime.setVisibility(View.GONE);
                return false;
            }
        });
        nameInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(nameInput.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    nameInput.setCursorVisible(false);
                    addTime.setVisibility(View.VISIBLE);
                }
                alarm.set_alarmName(v.getText().toString());
                return false;
            }
        });

        checkForFullWeek();

        //Hide button if the whole week has been assigned an alarm
        if (alarm.hasFullWeek) {
            addTime.setVisibility(View.GONE);
        } else {
            addTime.setVisibility(View.VISIBLE);
        }

        if (adapter == null) {
            adapter = new TimeAdapter(this, alarm.getAlarmTimes());
        }
        ListView listView = (ListView) findViewById(R.id.timesList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlarmTime alarmTime = (AlarmTime) adapterView.getItemAtPosition(i);
                openAddTime(alarmTime);
            }
        });

        //Listening to button event
        addTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View args) {
                if (editing) {
                    openAddTime(alarm);
                } else {
                    openAddTime();
                }
            }
        });

        //Hide the delete button if a new alarm is created
        ImageButton deleteBtn = (ImageButton) findViewById(R.id.delete_alarm);
        if (editing) {
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteBtn.setVisibility(View.GONE);
        }

    }

    //Run a check to see if the alarm has used up all the week days
    private void checkForFullWeek() {
        int countDays = 0;
        ArrayList<AlarmTime> alarmTimes = this.alarm.getAlarmTimes();
        for (int t = 0; t < alarmTimes.size(); t++) {
            boolean[] usedDays = alarmTimes.get(t).getDays();
            for (int d = 0; d < usedDays.length; d++) {
                if (usedDays[d]) {
                    countDays++;
                }
            }
        }
        this.alarm.hasFullWeek = (countDays >= 7);
    }



    //Open the add time activity when creating a new alarm
    private void openAddTime() {
        Intent addAlarmScreen = new Intent(getApplicationContext(), AlarmActivity_addAlarm.class);
        if(alarm.get_latlng() != null){
            addAlarmScreen.putExtra("activeAlarmLocation", Alarm.getConvertedLocation(alarm.get_latlng()));
            alarm.set_latlng(null);
        }
        addAlarmScreen.putExtra("activeAlarm", alarm);
        startActivity(addAlarmScreen);
    }

    //Open the add time activity when the alarm is being edited
    private void openAddTime(Alarm alarm) {
        Intent addAlarmScreen = new Intent(getApplicationContext(), AlarmActivity_addAlarm.class);
        addAlarmScreen.putExtra("activeAlarm", alarm);
        if(alarm.get_latlng() != null){
            addAlarmScreen.putExtra("activeAlarmLocation", Alarm.getConvertedLocation(alarm.get_latlng()));
            alarm.set_latlng(null);
        }
        addAlarmScreen.putExtra("editing_parent", editing);
        addAlarmScreen.putExtra("editing_name", oldAlarmName);
        startActivity(addAlarmScreen);
    }

    //Open the add time activity when alarm already contains other times
    private void openAddTime(AlarmTime alarmTime) {
        Intent addAlarmScreen = new Intent(getApplicationContext(), AlarmActivity_addAlarm.class);
        addAlarmScreen.putExtra("activeAlarm", alarm);
        if(alarm.get_latlng() != null){
            addAlarmScreen.putExtra("activeAlarmLocation", Alarm.getConvertedLocation(alarm.get_latlng()));
            alarm.set_latlng(null);
        }
        addAlarmScreen.putExtra("edit_time", alarmTime);
        addAlarmScreen.putExtra("editing_parent", editing);
        addAlarmScreen.putExtra("editing_name", oldAlarmName);
        startActivity(addAlarmScreen);
    }

    //Checks if the alarm has the required data for saving
    public String isValid() {
        if (this.alarm.get_alarmname() == null) {
            return "missing_alarm_name";
        } else if (this.alarm.get_latlng() == null){
            return "no_location";
        } else if (this.alarm.getAlarmTimes().size() <= 0) {
            return "no_times_set";
        } else if (!editing && (new AlarmDBHandler(getApplicationContext(), "", null, 8)).alarmExists(this.alarm.get_alarmname())) {
            return "alarm_name_exists";
        } else {
            return "valid";
        }
    }

    public void deleteAlarm(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("Are you sure you want do delete this alarm?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AlarmDBHandler dbHandler = new AlarmDBHandler(getApplicationContext(), alarm.get_alarmname(), null, 0);
                        dbHandler.deleteAlarm(alarm.get_alarmname());
                        startActivity(new Intent(getApplicationContext(), tabbedMain.class));
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void cancelAlarm(View view) {
       navigateUpTo(new Intent(getApplicationContext(), tabbedMain.class));
    }

    public void saveAlarm(View view) {
        String errorString = "";
        switch (isValid()) {
            case "valid":
                AlarmDBHandler alarmDBHandler = new AlarmDBHandler(getApplicationContext(), this.alarm.get_alarmname(), null, 0);
                if (editing) {
                    alarmDBHandler.updateAlarm(oldAlarmName, this.alarm);
                } else {
                    alarmDBHandler.addAlarm(this.alarm);
                }
                startActivity(new Intent(getApplicationContext(), tabbedMain.class));
                break;

            case "no_location":
                errorString = "The alarm needs a location. Add a location using the map.";
                break;

            case "no_times_set":
                errorString = "The alarm needs at least one time to be set";
                break;

            case "missing_alarm_name":
                errorString = "The alarm needs a name";
                break;

            case "alarm_name_exists":
                errorString = "The name already exists. Please add a unique alarm name.";
                break;
        }

        if (!errorString.equals("")) {
            Toast.makeText(this, errorString, Toast.LENGTH_SHORT).show();
        }

    }

    //Retrieving the pos of the device
    public LatLng getLocation() {
        LatLng location;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationPerm = true;
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PLACE_PICKER_REQUEST);
            }
        }

        // Set the map's camera position to the current location of the device.
        if (alarm.get_latlng() != null) {
            location = alarm.get_latlng();
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            addMapMarker(location);
            drawMapMarkerRadius(location);

        } else {
            location = new LatLng(55.6503358, 12.5410666);
            Log.d(TAG, "Current location is null. Using defaults.");
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 1));
        }

        return location;
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }

        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPerm = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PLACE_PICKER_REQUEST);
        }

        if (locationPerm) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            googleMap.setMyLocationEnabled(false);
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {}

    @Override
    public void onMarkerDrag(Marker marker) {}

    @Override
    public void onMarkerDragEnd(Marker marker) {}

    @Override
    public void onMapClick(LatLng pos) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(pos));
        this.alarm.set_latlng(pos);
        addMapMarker(pos);
        drawMapMarkerRadius(pos);
    }

    private void drawMapMarkerRadius(LatLng location){
        // draw circle
        int d = 500; // diameter
        Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint();
        p.setColor(getResources().getColor(R.color.cardview_dark_background));
        c.drawCircle(d / 2, d / 2, d / 2, p);

        // generate BitmapDescriptor from circle Bitmap
        BitmapDescriptor bmD = BitmapDescriptorFactory.fromBitmap(bm);
        googleMap.addGroundOverlay(new GroundOverlayOptions().
                image(bmD).
                position(location, AlarmLocationListener.SEARCH_RADIUS * 2, AlarmLocationListener.SEARCH_RADIUS * 2).
                transparency(0.4f));
    }

    private void addMapMarker(LatLng location){
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(location));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {}

    @Override
    public boolean onMyLocationButtonClick() {
        LatLng pos = new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
        this.alarm.set_latlng(pos);
        addMapMarker(pos);
        drawMapMarkerRadius(pos);
        return false;
    }
}