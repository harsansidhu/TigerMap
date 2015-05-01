package com.example.peter.tigermapsui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class DisplayDirectionActivity extends ActionBarActivity {
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_direction);

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(DirectionActivity.EXTRA_MESSAGE_FROM);
        String from = "From: ";
        String frommessage = from.concat(message);

        //Toast.makeText(getApplicationContext(),"From =" + message, Toast.LENGTH_SHORT).show();


        // Create the text view
        TextView textView = (TextView) findViewById(R.id.from);
        textView.setTextSize(24);
        textView.setText(frommessage);

        // Set the text view as the activity layout
       // setContentView(textView);

        // Get the message from the intent
        String tomessage = intent.getStringExtra(DirectionActivity.EXTRA_MESSAGE_TO);
        String to = "To: ";
        String tomess = to.concat(tomessage);

        //grab settings to concatenate settings;0;0;0;
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean wheel = settings.getBoolean("wheelMode", false);
        boolean grass = settings.getBoolean("grassMode", false);
        boolean buses = settings.getBoolean("busMode", false);

        boolean printers = settings.getBoolean("printersMode", false);
        boolean dining = settings.getBoolean("diningMode", false);
        String finloc = settings.getString("findlocMode", "");
        String wspeed = settings.getString("walkspeedMode", "");

        String sets = ";settings;";
        if(wheel) sets = sets.concat("1;");
        else sets = sets.concat("0;");
        if(grass) sets = sets.concat("1;");
        else sets = sets.concat("0;");
        if(buses) sets = sets.concat("1;");
        else sets = sets.concat("0;");

        if(finloc.isEmpty()) sets = sets.concat("3;");
        else sets = sets.concat(finloc + ";");

        if(printers) sets = sets.concat("1;");
        else sets = sets.concat("0;");
        if(dining) sets = sets.concat("1;");
        else sets = sets.concat("0;");

        if(wspeed.isEmpty()) sets = sets.concat("3.5;");
        else sets = sets.concat(wspeed + ";");
        //Toast.makeText(getApplicationContext(),"To =" + tomessage, Toast.LENGTH_SHORT).show();

        tomess = tomess.concat(sets);

        // Create the text view
        TextView textViewto = (TextView) findViewById(R.id.to);
        textViewto.setTextSize(24);
        textViewto.setText(tomess);

        // Set the text view as the activity layout
       // setContentView(textViewto);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_direction, menu);
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
