package com.example.peter.tigermapsui;

import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


public class SettingsActivity extends ActionBarActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //grab preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean stairs = settings.getBoolean("stairsMode", false);
        boolean wheel = settings.getBoolean("wheelMode", false);
        boolean grass = settings.getBoolean("grassMode", false);
        boolean buses = settings.getBoolean("busMode", false);

        boolean printers = settings.getBoolean("printersMode", false);
        boolean dining = settings.getBoolean("diningMode", false);

        String finloc = settings.getString("findlocMode", "");
        String wspeed = settings.getString("walkspeedMode", "");

        Switch wheelchair = (Switch) findViewById(R.id.wheel);
        Switch grasspath = (Switch) findViewById(R.id.grass);
        Switch bususe = (Switch) findViewById(R.id.buses);
        Switch onlineprint = (Switch) findViewById(R.id.printer);
        Switch opendine = (Switch) findViewById(R.id.dining);

        EditText findlocations = (EditText) findViewById(R.id.findloc);
        EditText walks = (EditText) findViewById(R.id.walkspeed);

        wheelchair.setChecked(wheel);
        grasspath.setChecked(grass);
        bususe.setChecked(buses);
        onlineprint.setChecked(printers);
        opendine.setChecked(dining);
        findlocations.setText(finloc, TextView.BufferType.EDITABLE);
        walks.setText(wspeed, TextView.BufferType.EDITABLE);

        wheelchair.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("wheelMode", isChecked);
            }
        });

        grasspath.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("grassMode", isChecked);
            }
        });

        bususe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("busesMode", isChecked);
            }
        });

        onlineprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("printersMode", isChecked);
            }
        });

        opendine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("diningMode", isChecked);
            }
        });

        findlocations.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("findlocMode", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        walks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("walkspeedMode", s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
