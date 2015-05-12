package com.example.odunayo.mapplus;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.Key;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

import static com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;

public class CurrentEventPage extends FragmentActivity implements LocationListener {
    private GoogleMap mMap;                   // stores the actual map
    private LocationManager locationManager;  // location manager
    private String provider;                  // either GPS or Network
    private Location location;                // stores location of user
    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private List<LatLng> latLngs = new ArrayList<LatLng>();
    private List<String> LatLngsStrings = new ArrayList<String>();
    private SharedPreferences settings;
    public static final String PREFS_NAME = "MyPrefsFile";
    private String start;
    private String dest;

    private class Marker {

        private LatLng location;
        private String name;
        private String Description;
        private Spanned content;

        public Marker(String name, String Description, LatLng location)
        {
            this.name = name;
            this.location =  location;
            this.Description = Description;
        }

        public String getName() {
            if (name.length() ==0)
                return "This is your Current Location";
            return name;
        }


        public LatLng getLatLng() {
            return location;
        }

        public String getDescription() {


            return Description;
        }

        public void setSpanned(Spanned content){
            this.content = content;

        }

        public Spanned getSpanned(){
            return content;

        }


    }

    private class Edge {

        private List<LatLng> edgeList;

        public Edge(List<LatLng> edgeList)
        {
            this.edgeList = edgeList;
        }

        public List<LatLng>  getEdges() {
            return edgeList;
        }


    }



    private class PopupAdapter implements GoogleMap.InfoWindowAdapter {
        private View popup=null;
        private LayoutInflater inflater=null;

        PopupAdapter(LayoutInflater inflater) {
            this.inflater=inflater;
        }

        @Override
        public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
            return(null);
        }

        @SuppressLint("InflateParams")
        @Override
        public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
            if (popup == null) {
                popup=inflater.inflate(R.layout.popup, null);
            }

            TextView tv=(TextView)popup.findViewById(R.id.title);

            tv.setText(marker.getTitle());
            tv=(TextView)popup.findViewById(R.id.snippet);
            tv.setText(marker.getSnippet());

            return(popup);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_current_event_page);
        setUpMapIfNeeded();

        // show location
        mMap.setMyLocationEnabled(true);

     //   mMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));

        mMap.setInfoWindowAdapter(new InfoWindowAdapter() {

            public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
                View v = getLayoutInflater().inflate(R.layout.custominfo, null);
                final String title = marker.getTitle();
                final TextView titleUi = ((TextView) v.findViewById(R.id.title));
                if (title != null) {
                    titleUi.setText(title);
                } else {
                    titleUi.setText("You Are Here");
                }

                String html = "(?<=[.])(?=[<])";


                final String snippet = marker.getSnippet();
                Log.d("Snippet ",snippet);

                //Check for first "<" and split there
                char[] charArray = snippet.toCharArray();
                int cIndex = 0;
                for(int i = 0; i < charArray.length; i++){
                    char c = charArray[i];
                    if (c == '<'){
                        cIndex = i;
                        break;
                    }


                }
                Log.d("c Index ", "Index " + cIndex);
                String sub = snippet.substring(cIndex);
                String snip = "";
                if (cIndex > 0)
                    snip = snippet.substring(0,cIndex-1);
                Log.d("startString ", snip);
                Log.d("subString ", sub);


                final TextView snippetUi = ((TextView) v
                        .findViewById(R.id.snippet));
                if (snippet != null) {
                    snippetUi.setText(snip); //formerly splitDescrip[0]
                } else {
                    snippetUi.setText("You Are Here");

                }

                final TextView htmlUi = ((TextView) v
                        .findViewById(R.id.html));
                if (!sub.isEmpty()) {
                    Spanned spannedContent = Html.fromHtml(sub);
                    htmlUi.setText(spannedContent, TextView.BufferType.SPANNABLE);

                } else {
                    htmlUi.setText("");
                }

                return v;
            }

            public View getInfoContents(com.google.android.gms.maps.model.Marker arg0) {

                View v = getLayoutInflater().inflate(R.layout.custominfo, null);

                return null;

            }
        });






        // initialize locationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider, 1000, 10, this);
        location = mMap.getMyLocation();


        // set camera to roughly the area of location
        location = mMap.getMyLocation();
        if (location == null) {
            // if location is null, then load the previous known location
            // even if inaccurate
            location = locationManager.getLastKnownLocation(provider);
        }

       if (location != null)
        onLocationChanged(location);

        locationManager.requestLocationUpdates(provider, 1000, 10, this);

        //Get shared Preferences from Settings
        settings = getSharedPreferences(PREFS_NAME, 0);
        boolean wheels = settings.getBoolean("wheelMode", false);
        boolean printers = settings.getBoolean("printersMode", false);
        boolean dining = settings.getBoolean("diningMode", false);
        boolean grass = settings.getBoolean("grassMode", false);
        String finloc = settings.getString("findlocMode", "3");
        String wspeed = settings.getString("walkspeedMode", "30");

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

        // get string origin and destination from NewEventPage
        Bundle extra = getIntent().getExtras();
        String origin = extra.getString("from");
        String destination = extra.getString("to");
        String[] destinations = extra.getStringArray("Locations");
        Boolean multiple = extra.getBoolean("multiple");
        String myLocation = extra.getString("mylocation");
        Boolean find = extra.getBoolean("findTrue");
        String findString = extra.getString("find");

        if (findString != null)
        Log.d("find ", findString);
        Log.d("findTrue ", "what " + find);


        String send = "";
        if (!find) {
            if (origin == "" || origin.equalsIgnoreCase("my location") || origin == "here" || origin == "BasedGod" || origin.equalsIgnoreCase("Current location"))
                origin = myLocation;
            start = origin;
            dest = destination;
            send = "dir;" + origin + ";" + destination + ";settings;" +
                    sWheels +";" + sGrass + ";0;" + wspeed + ";" + finloc +
                    ";" + sPrinters + ";" + sDining;
        }

        else {
            Log.d("changed: ", "changed send");
            send = findString;

        }
        // draws in the directions poly lines
        ServerCom s = new ServerCom();
        String response = null;
        try {
            response = s.sendToServer(this, origin, destination, send);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



        Log.d("RESPONSE FROM SERVER", response);
        CharSequence g = "Error";
       // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
        if (response.contains(g))
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

        else{

            //Parse String from server, absolute clusterfuck good luck.


            String delims = "[;]";
            String delims2 = "[:{}]+";

            String delims3 = "[)]+,[(]+";
            String delimcom = ",";
            String delimparn = "[(]";
            String delimparn2 = "[)]";

            //Split between Markers and Edges
            String[] firstSplit = response.split(delims);

            String markers = firstSplit[0];
            String dfirst2 = ",\\(";
           // String[] mTokens1 = markers.split(dfirst2);

            String delimit4 = "((([,][(]))|[{}])";
            String delimit5 = "\\[";
            String delimit6 = "[,]";
            String[] mTokens1 = markers.split(delimit4);



            List<Marker> mList = new ArrayList<Marker>();
            for(int i = 1; i < mTokens1.length; i++) {

                String mStr = mTokens1[i];
                Log.d("mStr " + i, mStr);
                String[] mTokens2 = mStr.split(delimit5);


                for(int j = 0; j < mTokens2.length; j++) {
                    Log.d("String " + j, mTokens2[j]);

                }

                String loc = mTokens2[0];
                String descrip = mTokens2[1];

                loc = loc.replace("(", "");
                loc = loc.replace(")", "");
                Log.d("loc " , loc);
                String[] splitloc = loc.split(delimit6);
                double mLat = Double.parseDouble(splitloc[0]);
                double mLon = Double.parseDouble(splitloc[1]);

                for(int j = 0; j < splitloc.length; j++) {
                    Log.d("String2 " + j, splitloc[j]);
                }


                Log.d("Descrip", descrip);

                String delimisolate = "\",\"";
                descrip = descrip.replace("]", "");
                String[] descrip2 = descrip.split(delimisolate); //formerly delimcom

                String name = descrip2[0];
                String description = "";
                if (descrip2.length > 1)
                    description = descrip2[1];

                name = name.replace("\"", "");
                description = description.replace("\"", "");

                for(int j = 0; j < descrip2.length; j++) {
                    Log.d("String3 " + j, descrip2[j]);
                }



                LatLng location = new LatLng(mLat, mLon);
                Marker m = new Marker(name, description, location);
                mList.add(m);


              /*  Toast.makeText(getApplicationContext(), "Marker: Name " + m.getName() +
                        " Marker: description " + m.getDescription() +
                        " Marker: LatLng " + m.getLatLng(),
                        Toast.LENGTH_LONG).show();*/

                Log.d("Marker: Name ", m.getName());
                Log.d("Marker: description ", m.getDescription());
                Log.d("Marker: LatLng ", m.getLatLng().toString());

            }

            String edges = firstSplit[1];
            String edgedelim = "[:{}]+";
            String[] tokens4 = edges.split(edgedelim);

            //LOOP FOR TESTING PURPOSES
            //for (int i = 0; i < tokens4.length; i++) {
             //   Log.d("Edges1 " + i, tokens4[i]);
           // }

            String xSplit = "X";
            if(tokens4.length > 1) {
                tokens4[1] = tokens4[1].replace(")),", "X");
                Log.d("Edge List Delim ", tokens4[1]);
            }


            String d = "[(())]+";
            List<Edge> edgeList = new ArrayList<Edge>();
            List<LatLng> eList = new ArrayList<LatLng>();

            if(tokens4.length > 1) {

                String[] splitLines = tokens4[1].split(xSplit);

                //LOOP FOR TESTING PURPOSES
                //for (int i = 0; i < splitLines.length; i++) {
                //    Log.d("Xsplit " + i, splitLines[i]);
               // }

                String zSplit = "Z";
                for (String eSplit : splitLines){
                    eSplit = eSplit.replace("(", "");
                    eSplit = eSplit.replace("))", "");
                    eSplit = eSplit.replace("),", "Z");
                   // Log.d("Xsplit2 ", eSplit);
                    List<LatLng> LatList = new ArrayList<LatLng>();
                    String[] p = eSplit.split(zSplit);
                    for (String str : p) {

                        if (!str.isEmpty()) {
                           // Log.d("Str ", str);
                            String[] q = str.split(delimcom);
                            double eLat = Double.parseDouble(q[0]);
                            double eLon = Double.parseDouble(q[1]);
                            LatLng eLatLng = new LatLng(eLat, eLon);
                         //   Log.d("LatList ", "Lat :" + eLatLng);
                            LatList.add(eLatLng);


                        }

                    }

                    Edge e =  new Edge(LatList);
                    edgeList.add(e);

                }



            }

           /* for (int i = 0; i < edgeList.size(); i++)
            {
                Edge e = edgeList.get(i);
                List<LatLng> edge = e.getEdges();
                for(LatLng lat : edge){
                    Log.d("Edge ", + i + " " + lat);
                }

            }*/

            addLines(mList, edgeList);

         }

        onLocationChanged(location);

    }

    /**
     * Adds a list of markers to the map.
     */
    public void addLines(List<Marker> mLatLngs, List<Edge> edgeList) {
        PolylineOptions options = new PolylineOptions();

        // light blue color and noticeable width
       // options.color(Color.parseColor("#DB3301"));
       // options.width(12);

        //Add Markers
        int i = 0;
        float color = BitmapDescriptorFactory.HUE_RED;
        for (Marker m : mLatLngs) {

            switch(i%6){
                case 0: color = BitmapDescriptorFactory.HUE_CYAN;
                    break;
                case 1: color = BitmapDescriptorFactory.HUE_ROSE;
                    break;
                case 2: color = BitmapDescriptorFactory.HUE_YELLOW;
                    break;
                case 3: color = BitmapDescriptorFactory.HUE_MAGENTA;
                    break;
                case 4: color = BitmapDescriptorFactory.HUE_GREEN;
                        break;
                case 5: color = BitmapDescriptorFactory.HUE_AZURE;
                    break;

            }

            if (i == 0) {

                mMap.addMarker(new MarkerOptions()
                        .title(m.getName())
                        .snippet(m.getDescription())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        .position(m.getLatLng()));
            }
            else
            mMap.addMarker(new MarkerOptions()
                    .title(m.getName())
                    .snippet(m.getDescription())
                    .icon(BitmapDescriptorFactory.defaultMarker(color))
                    .position(m.getLatLng()));
         i++;
        }


        //Add route path
        for(Edge e : edgeList){
            PolylineOptions polyLines = new PolylineOptions();
            polyLines.color(Color.parseColor("#DB3301"));
            polyLines.width(12);
            polyLines.addAll(e.getEdges());
            mMap.addPolyline(polyLines);

        }


    }

    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
        overridePendingTransition( R.layout.slide_in_up, R.layout.slide_out_down);
    }

    private void setUpMapIfNeeded() {
        if (mMap != null) {
            return;
        }
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap == null) {
            return;
        }
        // Initialize map options. For example:
        // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.current_event_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.cancel) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    public static class DirectionsResult {
        @Key("routes")
        public List<Route> routes;

    }

    public static class Route {
        @Key("overview_polyline")
        public OverviewPolyLine overviewPolyLine;
    }

    public static class OverviewPolyLine {
        @Key("points")
        public String points;
    }

    /**
     * Adds a list of markers to the map.
     */

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChanged(Location location) {



        GPSTracker gps = new GPSTracker(this);

        // Getting latitude of the current location, but set to princeton coordinates
        double latitude = 40.344601;

        // Getting longitude of the current location
        double longitude = -74.655595;

     /*   if(gps.canGetLocation()) {

                 latitude = gps.getLatitude();
                 longitude = gps.getLongitude();
            Log.d("Changed loc", "Changed loc");
        }*/

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);


        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        Log.d("Changed loc2", "Changed loc2");

        // stop
        locationManager.removeUpdates(this);
    }


}
