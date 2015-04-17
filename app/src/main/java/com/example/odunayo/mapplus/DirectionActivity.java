package com.example.peter.tigermapsui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class DirectionActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE_FROM = "com.example.peter.tigermapsui.MESSAGEFROM";
    public final static String EXTRA_MESSAGE_TO = "com.example.peter.tigermapsui.MESSAGETO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_direction, menu);
        return true;
    }

    public void direcsend(View view) {
        Intent intent = new Intent(this, DisplayDirectionActivity.class);
        EditText editText = (EditText) findViewById(R.id.fromdest);
        String frommessage = editText.getText().toString();

        // create class object
        // source for most of code below:
        // http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
        GPSTracker gps = new GPSTracker(DirectionActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()) {
            if (frommessage.equalsIgnoreCase("")) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                frommessage = "Lat: " + latitude + "\nLong: " + longitude;
            }
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        intent.putExtra(EXTRA_MESSAGE_FROM, frommessage);

        Toast.makeText(getApplicationContext(), "From =" + frommessage, Toast.LENGTH_SHORT).show();


        EditText editTextto = (EditText) findViewById(R.id.todest);
        String tomessage = editTextto.getText().toString();
        intent.putExtra(EXTRA_MESSAGE_TO, tomessage);

        Toast.makeText(getApplicationContext(),"To =" + tomessage, Toast.LENGTH_SHORT).show();

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
