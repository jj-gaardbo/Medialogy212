package com.example.jensjakupgaardbo.medialogy212;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

public class Gallery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

      /*  GridView gridView = (GridView) findViewById(R.id.gridView);

        gridView.setAdapter(new GalleryImageAdapter(this));*/
    }

    //TODO compress videos
    public void goToFire(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityVideoPage.class);
        startActivity(intent);
    }

    public void goToRain(View view) {
        Intent intent = new Intent(getApplicationContext(), ActivityVideoPage2.class);
        startActivity(intent);
    }
}
