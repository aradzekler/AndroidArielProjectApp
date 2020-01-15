package com.example.androidarielprojectapp.test;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import static com.example.androidarielprojectapp.test.NotificationActivity.CHANNEL_1_ID;


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
    String phoneNum = "";
    private NotificationManager notificationManager;
    DatabaseReference database =FirebaseDatabase.getInstance().getReference("rents");
    String currentRent ;
    FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();



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
        final Toast rentedMsg = Toast.makeText(MapsRentalActivity.this, "sorry vehicle is rented!", Toast.LENGTH_LONG);
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
            public boolean onMarkerClick(final Marker marker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsRentalActivity.this);
                builder.setTitle(R.string.dialog_rent_title);
                builder.setMessage(R.string.dialog_rent_msg);
                final String currentRent;
                for (RegisterNewRentDataObject object : mapObjectsList) {
                    if (object != null && object.getLat() == marker.getPosition().latitude &&
                            object.getLongi() == marker.getPosition().longitude) {
                        builder.setMessage("Phone: " + object.getPhone()
                                + "\n" + "Price: " + object.getPriceAsString()
                                + "\n" + object.getToolAsString());
                        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/" + object.getrentID() + "_150x150");
                        phoneNum = object.getPhone();
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
                                Call(phoneNum);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:

                                for (RegisterNewRentDataObject object : mapObjectsList) {
                                    if (object != null && object.getLat() == marker.getPosition().latitude &&
                                            object.getLongi() == marker.getPosition().longitude) {
                                        if(object.getRenterID().isEmpty()) //check if vehicle is rented
                                        {
                                            System.out.println(object.getrentID());
                                            database.child(object.getrentID()).child("renterID").setValue(currentuser.getUid());
                                            sendOnChannel1();
                                        }
                                        else{
                                            rentedMsg.show();
                                        }

                                    }
                                }

                                //System.out.println(currentRent);

                                break;
                            case DialogInterface.BUTTON_NEUTRAL:
                                // User clicked the No button
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setNegativeButton(R.string.dialog_rent_now, dialogClickListener);
                builder.setPositiveButton(R.string.dialog_call,dialogClickListener);
                builder.setNeutralButton(R.string.dialog_cancel, dialogClickListener);
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

    public  void Call(String phone) {
        // show() method display the toast with message
        // "clicked"
        Toast.makeText(this, "clicked", Toast.LENGTH_LONG)
                .show();
        // Use format with "tel:" and phoneNumber created is
        // stored in u.
        Uri u = Uri.parse("tel:" + phone);
        // Create the intent and set the data for the
        // intent as the phone number.
        Intent i = new Intent(Intent.ACTION_DIAL, u);
        try {
            // Launch the Phone app's dialer with a phone
            // number to dial a call.
            startActivity(i);
        }
        catch (SecurityException s) {
            // show() method display the toast with
            // exception message.

        }
    }


    public void sendOnChannel1(){//
        String title="Easy Rent";
        String message="someone wants to rent you'r vehicle!";
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
