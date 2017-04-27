package com.example.jensjakupgaardbo.medialogy212;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Created by Rasmus on 21-04-2017.
 */

public class AlarmReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        /*PackageManager pm = context.getPackageManager();
        Intent launchIntent = pm.getLaunchIntentForPackage("C:\\Users\\Rasmus\\Documents\\GitHub\\Medialogy212\\app\\src\\main\\java\\com\\example\\jensjakupgaardbo\\medialogy212\\dataBaseOverview.java");
        launchIntent.putExtra("some_data", "value");
        context.startActivity(launchIntent);
*/
        Intent openDb = new Intent(context, dataBaseOverview.class);
        context.startActivity(openDb);
        //Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
    }
}
