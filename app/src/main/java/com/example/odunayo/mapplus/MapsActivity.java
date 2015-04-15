package com.example.odunayo.mapplus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends ActionBarActivity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LocationManager locationManager;  // location manager
    private LocationListener locationListener;
    private String provider;                  // either GPS or Network
    private Location location;                // stores location of user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        // initialize locationManager
        final LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //locationListener = new Location
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        // set camera to roughly the area of location
        location = locationManager.getLastKnownLocation(provider);
        location = mMap.getMyLocation();

        LocationListener locationListener = new LocationListener() {
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
                // redraw the marker when get location update.
                drawMarker(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        locationManager.requestLocationUpdates(provider, 20000, 0, locationListener);

            if (location == null) {
               // Toast.makeText(getApplicationContext(), "fuck", Toast.LENGTH_LONG).show();
               // locationListener.onLocationChanged(location);
            }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng point) {
                mMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title("New position")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

            }
        });


    }

    private void drawMarker(Location location){
        // Remove any existing markers on the map
        mMap.clear();
        LatLng currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
        mMap.addMarker(new MarkerOptions()
                .position(currentPosition)
                .snippet("Lat:" + location.getLatitude() + "Lng:"+ location.getLongitude())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("ME"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        location = mMap.getMyLocation();
        if (location == null)
            Toast.makeText(getApplicationContext(), "Loc is null", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onMapReady(GoogleMap map) {
       mMap.setMyLocationEnabled(true);
       Location g =  locationManager.getLastKnownLocation(provider);

       //location =  mMap.getMyLocation();
        double lat = 0;
        double lon = 0;
        if (location != null) {
            Toast.makeText(getApplicationContext(), "fasdd", Toast.LENGTH_LONG).show();
             lat = location.getLatitude();
             lon = location.getLongitude();
        }
        LatLng myArea = new LatLng(lat, lon);

        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myArea, 13));

        map.addMarker(new MarkerOptions()
                .title("Here")
                .snippet("You Are Here")
                .position(myArea));
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }

        // show location
        mMap.setMyLocationEnabled(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }



   /* @Override
    public void onMapLongClick(LatLng point) {
        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

    }*/

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.new_event)
        {
            Intent intent = new Intent(this, NewEventPage.class);
            startActivity(intent);
        }

        if (id == R.id.current_event)
        {
            Intent intent = new Intent(this, CurrentEventPage.class);
            startActivity(intent);
            overridePendingTransition( R.layout.slide_in_up, R.layout.slide_out_down);
        }

        return true;
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
      //  onMapReady(mMap);
    }
}
