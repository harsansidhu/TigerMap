package com.example.odunayo.mapplus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Key;

public class NewEventPage extends FragmentActivity {
    private ArrayList<Friend> friendslist;  // list of selected friends
    private ArrayAdapter<Friend> adapter;   // adapter
    private ListView list;					// listview for friends

    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    protected static final int RESULT_CODE = 123;
    private AutoCompleteTextView from;
    private AutoCompleteTextView to;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.fragment_new_event_page);

        // initialize empty array list
        friendslist = new ArrayList<Friend>();

        // add functions for add buttons
        Button addFriend = (Button) findViewById(R.id.add_friend);
        Button addGroup = (Button) findViewById(R.id.add_group);

        // listens for add friend button
        addFriend.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                // send a request for an arraylist of friends to be returned
                // pushes in existing arraylist in case of multiple separate
                // occasions of adding friends
                Intent intent = new Intent(NewEventPage.this, SelectFriends.class);
                Bundle b = new Bundle();
                b.putParcelableArrayList("friends", friendslist);
                intent.putExtras(b);
                startActivityForResult(intent, 1);
            }
        });

        // listens for add group button
        addGroup.setOnClickListener(new OnClickListener() {
            public void onClick(View v)
            {
                // send a request for an arraylist of friends to be returned
                // pushes in existing arraylist in case of multiple separate
                // occasions of adding friends
               // Intent intent = new Intent(NewEventPage.this, SelectGroup.class);
               // Bundle b = new Bundle();
               // b.putParcelableArrayList("friends", friendslist);
               // intent.putExtras(b);
               // startActivityForResult(intent, 1);
            }
        });

        // listens for go button
        Button btnLoadDirections = (Button) findViewById(R.id.go);
        btnLoadDirections.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewEventPage.this, CurrentEventPage.class);

                // if the user specifies current location then use
                // current location
                if (from.getText().toString().equals("")) {
                    intent.putExtra("from", "");
                }

                else
                    intent.putExtra("from", from.getText().toString());

                intent.putExtra("to", to.getText().toString());
                startActivity(intent);

                finish();
            }
        });

        from = (AutoCompleteTextView) findViewById(R.id.from);
        to = (AutoCompleteTextView) findViewById(R.id.to);

        from.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
        to.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
    }

    public void onResume() {
        super.onResume();

        // sort and show
        Collections.sort(friendslist);
        populateListView();
    }


    // puts in friends
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if result is ok then read in the new list of friends
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            friendslist = bundle.getParcelableArrayList("friends");

            // updates the list
            adapter.notifyDataSetChanged();
        }
        if (resultCode == RESULT_CANCELED) {
            //Write your code if there's no result
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_event_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.cancel) {
            Toast.makeText(NewEventPage.this, "Event was canceled"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_new_event_page,
                    container, false);
            return rootView;
        }
    }

    // adds contacts into listview
    private void populateListView() {
        adapter = new MyListAdapter();
        list = (ListView) findViewById(R.id.added_friends);
        list.setAdapter(adapter);
    }

    // adapter for converting friends into listview
    private class MyListAdapter extends ArrayAdapter<Friend> {
        public MyListAdapter() {
            super(NewEventPage.this, R.layout.item_friend, friendslist);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // make sure we have a view to work with
            View itemView = convertView;
            final ViewHolder holder;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate
                        (R.layout.item_friend, parent, false);
                holder = new ViewHolder();
                holder.name = (TextView) itemView.findViewById(R.id.item_name);
                holder.icon = (ImageView) itemView.findViewById(R.id.item_icon);

                itemView.setTag(holder);
                itemView.setTag(R.id.item_name, holder.name);
                itemView.setTag(R.id.item_icon, holder.icon);
            }

            else {
                holder = (ViewHolder) itemView.getTag();
            }

            holder.name.setText(friendslist.get(position).getName());
            holder.icon.setImageResource(friendslist.get(position).getIcon());

            // listens for long click to delete friend
            list.setOnItemLongClickListener(new OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                               final int pos, long id) {
                    new AlertDialog.Builder(NewEventPage.this)
                            .setTitle("Delete entry")
                            .setMessage("Are you sure you want to delete " + friendslist.get(pos).getName() + "?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    Friend removed = adapter.getItem(pos);
                                    Toast.makeText(NewEventPage.this,
                                            removed.getName() + " was removed!", Toast.LENGTH_SHORT).show();
                                    adapter.remove(removed);
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                    dialog.cancel();
                                }
                            })
                            .setIcon(R.drawable.ic_dialog_alert_holo_light)
                            .show();

                    Log.v("long clicked","pos: " + pos);

                    return true;
                }
            });

            return itemView;
        }
    }

    public class ViewHolder {
        TextView name;
        ImageView icon;
    }

    // autocomplete
    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }

    private static final String PLACES_AUTOCOMPLETE_API = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";

    private ArrayList<String> autocomplete(String input) {

        ArrayList<String> resultList = new ArrayList<String>();

        try {

            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                                                                                        @Override
                                                                                        public void initialize(HttpRequest request) {
                                                                                            request.setParser(new JsonObjectParser(JSON_FACTORY));
                                                                                        }
                                                                                    }
            );

            GenericUrl url = new GenericUrl(PLACES_AUTOCOMPLETE_API);
            url.put("input", input);
            url.put("key", "AIzaSyB01dRPw8tO4dZ7GjQv4AgL9Lh3_fmBf8Q");
            url.put("sensor",false);

            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();
            PlacesResult directionsResult = httpResponse.parseAs(PlacesResult.class);

            List<Prediction> predictions = directionsResult.predictions;
            for (Prediction prediction : predictions) {
                resultList.add(prediction.description);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultList;
    }

    public static class PlacesResult {

        @Key("predictions")
        public List<Prediction> predictions;

    }

    public static class Prediction {
        @Key("description")
        public String description;

        @Key("id")
        public String id;
    }
}
