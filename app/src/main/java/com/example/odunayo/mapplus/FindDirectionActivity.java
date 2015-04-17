package com.example.peter.tigermapsui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class FindDirectionActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_direction);

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(FindActivity.EXTRA_MESSAGE_FROM);
        String from = "What: ";
        String frommessage = from.concat(message);

        //Toast.makeText(getApplicationContext(),"From =" + message, Toast.LENGTH_SHORT).show();


        // Create the text view
        TextView textView = (TextView) findViewById(R.id.what);
        textView.setTextSize(24);
        textView.setText(frommessage);

        // Set the text view as the activity layout
        // setContentView(textView);

        // Get the message from the intent
        String tomessage = intent.getStringExtra(FindActivity.EXTRA_MESSAGE_TO);
        String to = "Where: ";
        String tomess = to.concat(tomessage);

        //Toast.makeText(getApplicationContext(),"To =" + tomessage, Toast.LENGTH_SHORT).show();


        // Create the text view
        TextView textViewto = (TextView) findViewById(R.id.where);
        textViewto.setTextSize(24);
        textViewto.setText(tomess);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_direction, menu);
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
