package com.example.myvigilantapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import io.radar.sdk.Radar;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity<MainActivity> extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    NotificationManager NM;

    // set radius to draw circles around event

    boolean first = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Toast.makeText(MapsActivity.this, "firebase connection success",
                Toast.LENGTH_LONG).show();
        Radar.initialize("prj_test_pk_a61e4af4a3bd1e806a74126fb081873392d46d37");
        Toast.makeText(MapsActivity.this, "Radar Initialized",
                Toast.LENGTH_LONG).show();

        setContentView(R.layout.activity_maps);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        */

        onBackPressed();

    }


    private void fetchLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;
                        Toast.makeText(getApplicationContext(), currentLocation.getLatitude()+""+currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    supportMapFragment.getMapAsync(MapsActivity.this);
                }
            }


        });
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

        boolean permissionAccessFineLocationApproved =
                (androidx.core.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == android.content.pm.PackageManager.PERMISSION_GRANTED);


        if (permissionAccessFineLocationApproved==true) {



            // App can access location both in the foreground and in the background.
            // Start your service that doesn't have a foreground service type
            // defined.



            Radar.trackOnce(new io.radar.sdk.Radar.RadarCallback() {
                public void onComplete(io.radar.sdk.Radar.RadarStatus status,
                                       android.location.Location location, io.radar.sdk.model.RadarEvent[]
                                               events, io.radar.sdk.model.RadarUser user) {

                }
            });
            Radar.startTracking();



        } else {
            // App doesn't have access to the device's location at all. Make full request
            // for permission.

        }

        fetchLastLocation();

        updateMap(googleMap);

        markLocation(43.4706268, -80.5434259, 109);
        markLocation(43.4729791, -80.54010269999999, 56);

    }

    private void updateMap (GoogleMap googleMap)  {


        mMap = googleMap;

        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

        // replace coordinates with coordinates of current location when we have that figured out

        mMap.addMarker(new MarkerOptions().position(latLng).title("Your current location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        if (first) {

            float zoomLevel = 16.0f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));

            first = false;

        }



        List<LatLng> points = new ArrayList<>();
        points.add(new LatLng(43.472428, -80.540136));
        points.add(new LatLng(43.473207, -80.540683));
        points.add(new LatLng(43.473374, -80.540144));
        points.add(new LatLng(43.472652, -80.539481));

        boolean contain = (latLng.latitude != latLng.longitude);


        if(contain == true){
            /// do something
        } else {

        }

        /* Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        */
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_CODE:
                if (grantResults.length>0 &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }

    }

    private void markLocation(double x, double y, int radius){
        LatLng newLocation = new LatLng(x, y);
        mMap.addMarker(new MarkerOptions().position(newLocation).title("Police Reported Incident"));
        drawCircle(new LatLng(x, y), radius);
    }
    private void drawCircle(LatLng point, int radius){

        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(radius);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x20fbbb3A);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);

    }
    public void onBackPressed()
    {

        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(MapsActivity.this);

        // Set the message show for the Alert time
        builder.setMessage("You are entering a police marked area!");

        // Set Alert Title
        builder.setTitle("Caution!");

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Acknowledge",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {

                                // When the user click yes button
                                // then app will close

                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.


        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
}
