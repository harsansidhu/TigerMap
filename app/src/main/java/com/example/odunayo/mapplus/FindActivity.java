package com.example.peter.tigermapsui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class FindActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    public final static String EXTRA_MESSAGE_FROM = "com.example.peter.tigermapsui.MESSAGEFIND";
    public final static String EXTRA_MESSAGE_TO = "com.example.peter.tigermapsui.MESSAGETOF";

    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.find_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
    }

    public void fromsend(View view) {
        Intent intent = new Intent(this, FindDirectionActivity.class);
        String frommessage = value;
        intent.putExtra(EXTRA_MESSAGE_FROM, frommessage);

        Toast.makeText(getApplicationContext(), "From =" + frommessage, Toast.LENGTH_SHORT).show();


        EditText editTextnear = (EditText) findViewById(R.id.neardest);
        String tomessage = editTextnear.getText().toString();
        intent.putExtra(EXTRA_MESSAGE_TO, tomessage);

        // http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
        GPSTracker gps = new GPSTracker(FindActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()) {
            if (tomessage.equalsIgnoreCase("")) {

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                // \n is for new line
                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                tomessage = "Lat: " + latitude + "\nLong: " + longitude;
            }
        }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
        }


        Toast.makeText(getApplicationContext(),"To =" + tomessage, Toast.LENGTH_SHORT).show();

        startActivity(intent);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
         value = parent.getItemAtPosition(pos).toString();

        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

    }

    public void onNothingSelected(AdapterView<?> parent) {
        value = "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find, menu);
        return true;
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
