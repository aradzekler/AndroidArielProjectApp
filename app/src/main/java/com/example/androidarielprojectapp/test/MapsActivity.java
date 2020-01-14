package com.example.androidarielprojectapp.test;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.androidarielprojectapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * Activity for setting up a map
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Location mLastLocation;
    Marker mCurrLocationMarker; // our current location
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Button newRentalActivityButton;
    ArrayList<Marker> markersToClear = new ArrayList<Marker>(); // arraylist for clearing markers from the map
    private GoogleMap mMap;
    private Marker rMapMarker;
    private FusedLocationProviderClient fusedLocationClient;
    private int MY_PERMISSION_CODE = 666;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        newRentalActivityButton = (Button) findViewById(R.id.new_rent_activity_button);
        //TODO: allow only registered users to access registering.

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        System.out.println(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION));
        System.out.println(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION));
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(false);
            }
        }
        ActivityCompat.requestPermissions(MapsActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_CODE);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mMap.setMyLocationEnabled(false);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                Marker mapMarker;
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng).title("Rent Location")
                        .snippet("the current location of your rental")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon_2))
                        .draggable(true)
                        .anchor(0.5f, 0.5f);


                // Clears the previously touched position
                for (Marker marker : markersToClear) {
                    marker.remove();
                }

                mapMarker = mMap.addMarker(markerOptions);
                markersToClear.add(mapMarker);

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                rMapMarker = mapMarker;


            }
        });


        // start a new rental
        newRentalActivityButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), NewRentalActivity.class);
                try {
                    double[] coor = {rMapMarker.getPosition().latitude, rMapMarker.getPosition().longitude};
                    i.putExtra("RENTAL_LOCATION", coor);
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
                startActivity(i);
            }
        });


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        System.out.println("#############");
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    // for marking our location on the map.
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title("You are here!")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker_3))
                .draggable(false)
                .anchor(0.5f, 0.5f);
        try {
            mCurrLocationMarker = mMap.addMarker(markerOptions);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        }

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
