package com.example.peter.tigermapsui;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class DisplayDirectionActivity extends ActionBarActivity {

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

        //Toast.makeText(getApplicationContext(),"To =" + tomessage, Toast.LENGTH_SHORT).show();


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
