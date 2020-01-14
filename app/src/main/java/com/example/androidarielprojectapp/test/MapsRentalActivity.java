package com.example.androidarielprojectapp.test;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.example.androidarielprojectapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * Activity for setting up a map that shows rents
 */
public class MapsRentalActivity extends FragmentActivity implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    Location mLastLocation;
    Marker mCurrLocationMarker; // our current location
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private GoogleMap mMap;
    private int MY_PERMISSION_CODE = 666;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent intent = getIntent();
        final ArrayList<RegisterNewRentDataObject> mapObjectsList = (ArrayList<RegisterNewRentDataObject>) intent
                .getSerializableExtra("QUERY_USERS");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(false);
            }
        }
        ActivityCompat.requestPermissions(MapsRentalActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_CODE);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mMap.setMyLocationEnabled(false);

        for (RegisterNewRentDataObject object : mapObjectsList) {
            if (object != null) {
                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(object.getLat(), object.getLongi());
                markerOptions.position(latLng).title(object.getPhone())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_icon_2))
                        .draggable(false)
                        .anchor(0.5f, 0.5f);
                mMap.addMarker(markerOptions).setTag(latLng);

            }
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsRentalActivity.this);
                builder.setTitle(R.string.dialog_rent_title);
                builder.setMessage(R.string.dialog_rent_msg);

                for (RegisterNewRentDataObject object : mapObjectsList) {
                    if (object != null && object.getLat() == marker.getPosition().latitude &&
                            object.getLongi() == marker.getPosition().longitude) {
                        builder.setMessage("Phone: " + object.getPhone()
                                + "\n" + "Price: " + object.getPriceAsString()
                                + "\n" + object.getToolAsString());
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + object.getrentID() + "_150x150");
                        ImageView imageView = new ImageView(MapsRentalActivity.this);
                        LayoutInflater factory = LayoutInflater.from(MapsRentalActivity.this);
                        ImageView image = (ImageView) factory.inflate(R.layout.singleimage, null);
                        // Load the image using Glide
                        try {
                            GlideApp.with(MapsRentalActivity.this)
                                    .load(storageReference)
                                    .centerCrop()
                                    .into(image);
                            //builder.setIcon(image.getDrawable());
                        } catch (NullPointerException npe) {
                            npe.printStackTrace();
                        }
                        builder.setView(image);
                    }
                }

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                //TODO: add notifications and db removal of values.
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // User clicked the No button
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton(R.string.dialog_rent_now, dialogClickListener);
                builder.setNegativeButton(R.string.dialog_cancel, dialogClickListener);
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
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

        //Place rents location markers.
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
