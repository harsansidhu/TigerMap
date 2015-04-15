package com.example.odunayo.mapplus;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

public class CurrentEventPage extends FragmentActivity implements LocationListener {
    private GoogleMap mMap;                   // stores the actual map
    private LocationManager locationManager;  // location manager
    private String provider;                  // either GPS or Network
    private Location location;                // stores location of user
    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private List<LatLng> latLngs = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_current_event_page);
        setUpMapIfNeeded();

        // show location
        mMap.setMyLocationEnabled(true);

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

        onLocationChanged(location);

        locationManager.requestLocationUpdates(provider, 1000, 10, this);

        // get string origin and destination from NewEventPage
        Bundle extra = getIntent().getExtras();
        String origin = extra.getString("from");
        String destination = extra.getString("to");

        // if user doesnt input a location then get latLng
        if (origin.equals("")) {
            StringBuilder s = new StringBuilder();
            s.append(location.getLatitude());
            s.append(",");
            s.append(location.getLongitude());
            origin = s.toString();
        }

        // draws in the directions poly lines
        DirectionsFetcher df = new DirectionsFetcher(origin, destination);
        df.execute();
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

    private class DirectionsFetcher extends AsyncTask<URL, Integer, Void> {
        private String origin;
        private String destination;

        public DirectionsFetcher(String origin,String destination) {
            this.origin = origin;
            this.destination = destination;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CurrentEventPage.this.setProgressBarIndeterminateVisibility(Boolean.TRUE);
        }

        protected Void doInBackground(URL... urls) {
            try {
                HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest request) {
                        request.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });

                GenericUrl url = new GenericUrl("http://maps.googleapis.com/maps/api/directions/json?");
                url.put("origin", origin);
                url.put("destination", destination);
                url.put("sensor",false);
                url.put("mode", "walking");

                HttpRequest request = requestFactory.buildGetRequest(url);
                HttpResponse httpResponse = request.execute();
                DirectionsResult directionsResult = httpResponse.parseAs(DirectionsResult.class);

                String encodedPoints = directionsResult.routes.get(0).overviewPolyLine.points;
                latLngs = PolyUtil.decode(encodedPoints);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;

        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Void result) {
            System.out.println("Adding polyline");
            addPolylineToMap(latLngs);
            System.out.println("Fix Zoom");
            GoogleMapUtils.fixZoomForLatLngs(mMap, latLngs);
            CurrentEventPage.this.setProgressBarIndeterminateVisibility(Boolean.FALSE);
        }
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
    public void addPolylineToMap(List<LatLng> latLngs) {
        PolylineOptions options = new PolylineOptions();

        // light blue color and noticeable width
        options.color(Color.parseColor("#DB3301"));
        options.width(12);

       // for (LatLng latLng : latLngs) {
       //     options.add(latLng);
       // }
        LatLng start = new LatLng(40.348775,-74.658477);
        LatLng dest = new LatLng(40.348245,-74.656751);
        options.add(new LatLng(40.348775,-74.658477));
        options.add(new LatLng(40.348630,-74.658424));
        options.add(new LatLng(40.348758,-74.657938));
        options.add(new LatLng(40.348298,-74.657721));
        options.add(new LatLng(40.348265,-74.657556));
        options.add(new LatLng(40.348327,-74.656871));
        options.add(new LatLng(40.348245,-74.656751));
        mMap.addMarker(new MarkerOptions()
                .title("Start")
                .snippet("East Pyne")
                .position(start));
        mMap.addMarker(new MarkerOptions()
                .title("Destination")
                .snippet("Mccosh")
                .position(dest));
        mMap.addPolyline(options);
    }

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
        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        // stop
        locationManager.removeUpdates(this);
    }
}
