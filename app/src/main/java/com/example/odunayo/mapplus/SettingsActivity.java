package com.example.odunayo.mapplus;


import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends ActionBarActivity {
    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //grab preferences
        settings = getSharedPreferences(PREFS_NAME, 0);
        boolean wheel = settings.getBoolean("wheelMode", false);
        boolean grass = settings.getBoolean("grassMode", false);
        boolean buses = settings.getBoolean("busMode", false);

        boolean printers = settings.getBoolean("printersMode", false);
        boolean dining = settings.getBoolean("diningMode", false);

        String finloc = settings.getString("findlocMode", "30");
        String wspeed = settings.getString("walkspeedMode", "3");
       // Float wspeed = settings.getFloat("walkspeedMode", 30);
       // Integer finloc = settings.getInt("findlocMode", 3);

        Switch wheelchair = (Switch) findViewById(R.id.wheel);
        Switch grasspath = (Switch) findViewById(R.id.grass);
        //Switch bususe = (Switch) findViewById(R.id.buses);
        Switch onlineprint = (Switch) findViewById(R.id.printer);
        Switch opendine = (Switch) findViewById(R.id.dining);

        Button submit = (Button) findViewById(R.id.submit);

        EditText findlocations = (EditText) findViewById(R.id.findloc);
        EditText walks = (EditText) findViewById(R.id.walkspeed);

        wheelchair.setChecked(wheel);
        grasspath.setChecked(grass);
       // bususe.setChecked(buses);
        onlineprint.setChecked(printers);
        opendine.setChecked(dining);
        findlocations.setText(finloc, TextView.BufferType.EDITABLE);
       // walks.setText(wspeed, TextView.BufferType.EDITABLE);
        walks.setText(String.valueOf(wspeed));

        wheelchair.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("wheelMode", isChecked);
                editor.commit();

              //  Toast.makeText(getApplicationContext(), "Wheels", Toast.LENGTH_LONG).show();
            }
        });

        grasspath.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("grassMode", isChecked);
                editor.commit();
            }
        });

     /*   bususe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("busMode", isChecked);
                editor.commit();
            }
        });*/

        onlineprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("printersMode", isChecked);
                editor.commit();
            }
        });

        opendine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("diningMode", isChecked);
                editor.commit();
            }
        });

        findlocations.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("findlocMode", s.toString());
                editor.commit();
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
                //settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("walkspeedMode", s.toString());
                editor.commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = settings.getString("findlocMode", "3");
                String speed = settings.getString("walkspeedMode", "30");
                if (loc.isEmpty() || loc.matches("0"))
                    Toast.makeText(getApplication(), "# of locations must be greater than 0", Toast.LENGTH_SHORT).show();
                else if (speed.isEmpty() || speed.matches("0"))
                    Toast.makeText(getApplication(), "speed must be greater than 0", Toast.LENGTH_SHORT).show();
                else
                finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
        //Disables when back button is Pressed
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_settings, menu);
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