package com.example.odunayo.mapplus;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class DirectionsActivity extends ActionBarActivity {
    public final static String EXTRA_MESSAGE_FROM = "com.example.peter.tigermapsui.MESSAGEFROM";
    public final static String EXTRA_MESSAGE_TO = "com.example.peter.tigermapsui.MESSAGETO";

    private AutoCompleteTextView[] added = new AutoCompleteTextView[5];
    private int index = 0;
    private LinearLayout layout;
    private Button addEditText;
    private Button removeEditText;
    private Button directions;
    private String LatLng;
    private String[] destArray;
    private Boolean mult =  false;
    private AutoCompleteTextView editText;
    private AutoCompleteTextView editTextto;
    //private EditText editTextto;
    private String tomessage;
    private String frommessage;
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);

        //Autocomplete Adapter
        String[] completion = getResources().getStringArray(R.array.autocompletearray);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,completion);


        layout=(LinearLayout)findViewById(R.id.layout);
        TextView directions = (TextView)findViewById(R.id.textView3);

        directions = (Button) findViewById(R.id.getdirec);
        addEditText = (Button) findViewById(R.id.addEditBut);
        removeEditText = (Button) findViewById(R.id.removeEditBut);
        removeEditText.setEnabled(false);

        editTextto = (AutoCompleteTextView) findViewById(R.id.todest);
        editTextto.setAdapter(adapter);
        editTextto.setThreshold(1);

        editText = (AutoCompleteTextView) findViewById(R.id.fromdest);
        editText.setAdapter(adapter);
        editText.setThreshold(1);

        added[0] =(AutoCompleteTextView) findViewById(R.id.addedEdit1);
        added[0].setAdapter(adapter);
        added[1] =(AutoCompleteTextView) findViewById(R.id.addedEdit2);
        added[1].setAdapter(adapter);
        added[2] =(AutoCompleteTextView) findViewById(R.id.addedEdit3);
        added[2].setAdapter(adapter);
        added[3] =(AutoCompleteTextView) findViewById(R.id.addedEdit4);
        added[3].setAdapter(adapter);
        added[4] =(AutoCompleteTextView) findViewById(R.id.addedEdit5);
        added[4].setAdapter(adapter);

        addEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //added[index].setId("added[index]" + "layout".getChildCount()+1);
                added[index].setVisibility(View.VISIBLE);
                index++;
                //if index > 0, restore clickability to removeEdit
                if(index > 0) removeEditText.setEnabled(true);
                //if index == 5 remove clickability of addEdit
                if(index == 5) addEditText.setEnabled(false);
            }
        });

        removeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                added[index-1].setVisibility(View.GONE);
                index--;
                //if index == 0, remove clickability
                if(index == 0) removeEditText.setEnabled(false);
                if(index < 5) addEditText.setEnabled(true);
            }
        });


    //   s = s.replace("\",", "</item>");
     //  s = s.replace("\"", "<item>");

       // Log.d("", s);




            directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), CurrentEventPage.class);

                frommessage = editText.getText().toString().replace(";", "");
                tomessage = editTextto.getText().toString().replace(";","");
                for(int i = 0; i < added.length; i++) {
                    if (added[i].getVisibility() == View.VISIBLE) {
                  /*      if(tomessage.contains(";"))
                        {
                            Toast.makeText(getApplicationContext(), "You Cannot have SemiColons in your directions", Toast.LENGTH_LONG).show();
                            finish();
                        }*/
                        tomessage = tomessage.concat(";" + added[i].getText().toString().replace(";", ""));
                        mult = true;

                    }
                }


                GPSTracker gps = new GPSTracker(DirectionsActivity.this);

                // check if GPS enabled
                if(gps.canGetLocation()) {
                    if (frommessage.equalsIgnoreCase("") || frommessage.equalsIgnoreCase("my location") || frommessage.equalsIgnoreCase("BasedGod") ||frommessage.equalsIgnoreCase("current location") ) {

                        double latitude = gps.getLatitude();
                        double longitude = gps.getLongitude();

                        //Create String for LatLng
                        LatLng = "(" + latitude + " ," + longitude +")";
                        //   Toast.makeText(getApplicationContext(), "Location" + LatLng, Toast.LENGTH_LONG).show();
                        intent.putExtra("mylocation", LatLng);

                        // \n is for new line
                        // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                        frommessage = "(" + latitude + "," + longitude + ")";
                    }
                }else{
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    Toast.makeText(getApplicationContext(), "Cant get location", Toast.LENGTH_LONG).show();
                    gps.showSettingsAlert();
                }



           //     Log.d("From2 " , frommessage[0]);
           //     Log.d("To " , finalTomessage);
             //   Log.d("From ", editText.getText().toString());
             //   Toast.makeText(getApplicationContext(),"edittext " +   editTextto.getText().toString(), Toast.LENGTH_SHORT).show();
             //   Log.d("To ", tomessage);

                intent.putExtra("from", frommessage);
                intent.putExtra("to", tomessage);
                intent.putExtra("multiple", mult);
                startActivity(intent);

            }
        });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_direction, menu);
        return true;
    }

    /*
    public void addEdit(View view) {
        added[index] =new EditText(this);
        added[index].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        layout.addView(added[index]);
        index++;
        //if index > 0, restore clickability to removeEdit
        if(index > 0) removeEditText.setEnabled(true);
        //if index == 5 remove clickability of addEdit
        if(index == 5) addEditText.setEnabled(false);
    } */
    /*
    public void removeEdit(View view) {
        layout.removeView(added[index-1]);
        index--;
        //if index == 0, remove clickability
        if(index == 0) removeEditText.setEnabled(false);
        if(index < 5) addEditText.setEnabled(true);
    }
*/
    public void direcsend(View view) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       /* //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }
}