package com.labs.nathan.ntbrookslab9;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, View.OnLongClickListener {

    private GoogleMap mMap;
    private float mZoom;
    private ArrayList<MarkerOptions> mMarkers; // markers location list
    private ArrayList<Marker> moMarkers; // markser object list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findViewById(R.id.button_plus).setOnClickListener(this);
        findViewById(R.id.button_minus).setOnClickListener(this);
        findViewById(R.id.button_change).setOnClickListener(this);
        findViewById(R.id.button_change).setOnLongClickListener(this);
        findViewById(R.id.button_mark).setOnClickListener(this);
        findViewById(R.id.button_mark).setOnLongClickListener(this);


        if(savedInstanceState != null) {
            mMarkers = (ArrayList<MarkerOptions>) savedInstanceState.getSerializable("markers");
        } else {
            mMarkers = new ArrayList();
        }

        moMarkers = new ArrayList();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("markers", mMarkers);
    }

    private String getProvider(LocationManager locationManager) {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true);

        if(bestProvider != null)
            return bestProvider;
        else
            return LocationManager.GPS_PROVIDER;
    }

    private void removeLastMark() {
        if(mMarkers.size() > 0) {
            mMarkers.remove(mMarkers.size()-1);
            moMarkers.get(moMarkers.size()-1).remove();
            moMarkers.remove(moMarkers.size()-1);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch(v.getId()) {
            case R.id.button_mark:
                removeLastMark();
                return true;
            case R.id.button_change:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("About")
                        .setMessage("Nathan T. Brooks, CSCD 372, Fall 2015, Lab 9")
                        .setNeutralButton("OK", null)
                        .setCancelable(false)
                        .create()
                        .show();
                return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.button_plus:
                onPlus();
                break;
            case R.id.button_minus:
                onMinus();
                break;
            case R.id.button_change:
                onChange();
                break;
            case R.id.button_mark:
                onMark();
                break;
            default:
        }
    }

    private void onPlus() {
        mMap.moveCamera(CameraUpdateFactory.zoomIn()) ;
        mZoom = mMap.getCameraPosition().zoom ;
    }
    private void onMinus() {
        mMap.moveCamera(CameraUpdateFactory.zoomOut()) ;
        mZoom = mMap.getCameraPosition().zoom ;
    }

    private void onChange() {
        switch(mMap.getMapType()) {
            case GoogleMap.MAP_TYPE_NORMAL:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case GoogleMap.MAP_TYPE_SATELLITE:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case GoogleMap.MAP_TYPE_TERRAIN:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            default:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    private void onMark() {
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if(getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(getProvider(locationManager));
            if(location != null) {
                LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latlng);
                markerOptions.title("Marker" + (mMarkers.size() + 1));

                mMarkers.add(markerOptions);
                moMarkers.add(mMap.addMarker(markerOptions));
            } else {
                Toast.makeText(this, "Cannot find location", Toast.LENGTH_LONG).show();
            }
        } else
        Toast.makeText(this, "Do not have permission for fine location", Toast.LENGTH_LONG).show();
    }

    private void initMap(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        if(getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getPackageName()) == PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(getProvider(locationManager));
            if(location != null) {
                LatLng latlng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
            } else {
                Toast.makeText(this, "Could not find location", Toast.LENGTH_LONG).show();
            }
        } else
            Toast.makeText(this, "Do not have permission for fine location", Toast.LENGTH_LONG).show();

        for(MarkerOptions marker : mMarkers) {
            moMarkers.add(mMap.addMarker(marker));
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        initMap(googleMap);
    }
}
