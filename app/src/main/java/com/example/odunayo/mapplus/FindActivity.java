package com.example.odunayo.mapplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class FindActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {
    public final static String EXTRA_MESSAGE_FROM = "com.example.peter.tigermapsui.MESSAGEFIND";
    public final static String EXTRA_MESSAGE_TO = "com.example.peter.tigermapsui.MESSAGETOF";

    private String value;
    private Boolean find = true;
    private int position;
    private AutoCompleteTextView findText;
    private ArrayAdapter<String> autoadapter;
    private SharedPreferences settings;
    public static final String PREFS_NAME = "MyPrefsFile";

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

        //Autocomplete Adapter
        String[] completion = getResources().getStringArray(R.array.autocompletearray);
        autoadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,completion);

        findText = (AutoCompleteTextView) findViewById(R.id.neardest);
        findText.setAdapter(autoadapter);
        findText.setThreshold(3);




        Log.d("Find ID ", String.valueOf(spinner.getSelectedItemId()));
    }

    public void fromsend(View view) {
        Intent intent = new Intent(this, CurrentEventPage.class);
        String frommessage = value;
        intent.putExtra(EXTRA_MESSAGE_FROM, frommessage);

        settings = getSharedPreferences(PREFS_NAME, 0);
        boolean wheels = settings.getBoolean("wheelMode", false);
        boolean printers = settings.getBoolean("printersMode", false);
        boolean dining = settings.getBoolean("diningMode", false);
        boolean grass = settings.getBoolean("grassMode", false);
        String finloc = settings.getString("findlocMode", "30");
        String wspeed = settings.getString("walkspeedMode", "3");

        String sWheels = "0";
        String sPrinters = "0";
        String sDining = "0";
        String sGrass = "0";
        if (wheels)
            sWheels = "1";
        if (printers)
            sPrinters = "1";
        if (dining)
            sDining = "1";
        if (grass)
            sGrass = "1";
        if (finloc.isEmpty())
            finloc = "0";
        if (wspeed.isEmpty())
            wspeed = "0";

        Log.d("Wheels changed", "Boolean " + wheels);
        Log.d("printers changed", "Boolean " + printers);
        Log.d("dining changed", "Boolean " + dining);
        Log.d("Wheels changed", "String " + sWheels);
        Log.d("printers changed", "String " + sPrinters);
        Log.d("dining changed", "String " + sDining);
        Log.d("grass changed", "Boolean " + grass);
        Log.d("grass changed", "String " + sGrass);

        Log.d("finloc", "String " + finloc);
        Log.d("wspeed", "String " + wspeed);

      //  Toast.makeText(getApplicationContext(), "From =" + frommessage, Toast.LENGTH_SHORT).show();


      //  EditText editTextnear = (EditText) findViewById(R.id.neardest);
      //  String tomessage = editTextnear.getText().toString();
         String tomessage = findText.getText().toString();


        //GPS Code Adapted From:
        // http://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
        //Last Access 5/1/15
        GPSTracker gps = new GPSTracker(FindActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()) {
            if (tomessage.equalsIgnoreCase("") || tomessage.equalsIgnoreCase("my location") || tomessage.equalsIgnoreCase("current location")) {
                if(tomessage.contains(";"))
                {
                    Toast.makeText(getApplicationContext(), "You Cannot have SemiColons in your directions", Toast.LENGTH_LONG).show();
                    finish();
                }

                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                // \n is for new line
              //  Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
              //  tomessage = "Lat: " + latitude + "\nLong: " + longitude;
                tomessage = "(" + latitude + " ," + longitude +")";
            }
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

      //  String formatted = "find;" + position + ";" + tomessage + ";settings;0;1;0;2.5;3;1;0";
        String formatted = "find;" + position + ";" + tomessage + ";settings;" +
                sWheels +";" + sGrass + ";0;" + wspeed + ";" + finloc +
                ";" + sPrinters + ";" + sDining;

        intent.putExtra(EXTRA_MESSAGE_TO, tomessage);
        intent.putExtra("findTrue", find);
        intent.putExtra("find", formatted);
       // Toast.makeText(getApplicationContext(), formatted, Toast.LENGTH_LONG).show();


     //   Toast.makeText(getApplicationContext(),"To =" + tomessage, Toast.LENGTH_SHORT).show();


        startActivity(intent);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        value = parent.getItemAtPosition(pos).toString();
        position = pos;

       // Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
      //  Toast.makeText(getApplicationContext(), "Position " + position, Toast.LENGTH_SHORT).show();

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