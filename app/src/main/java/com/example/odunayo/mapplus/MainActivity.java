package com.example.peter.tigermapsui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button direction = (Button) findViewById(R.id.Direcbutton);
        Button find = (Button) findViewById(R.id.Findbutton);
        Button dummy2 = (Button) findViewById(R.id.button3);
        Button settings = (Button) findViewById(R.id.settings);

        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Directions clicked", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplication(), DirectionActivity.class);
                //EditText editText = (EditText) findViewById(R.id.edit_message);
                startActivity(intent);
            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Find clicked", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplication(), FindActivity.class);
                //EditText editText = (EditText) findViewById(R.id.edit_message);
                startActivity(intent);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SettingsActivity.class);
                //EditText editText = (EditText) findViewById(R.id.edit_message);
                startActivity(intent);
            }
        });

        dummy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                boolean stairs = settings.getBoolean("wheelMode", false);
                String wspeed = settings.getString("walkspeedMode", "");

                Toast.makeText(getApplicationContext(),"wheelchair: " + stairs +
                        "\nwalk speed: " + wspeed, Toast.LENGTH_LONG).show();
            }
        });
    }
    /*click directions button to go to the page
    public void direcpage(View view) {
        Intent intent = new Intent(this, DirectionActivity.class);
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        startActivity(intent);
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
