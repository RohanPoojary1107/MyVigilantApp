package com.example.myvigilantapp;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.radar.sdk.Radar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toast.makeText(MapsActivity.this, "firebase connection success",
                Toast.LENGTH_LONG).show();
        Radar.initialize("prj_test_pk_a61e4af4a3bd1e806a74126fb081873392d46d37");


        boolean permissionAccessFineLocationApproved =
                androidx.core.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        == android.content.pm.PackageManager.PERMISSION_GRANTED;

        if (permissionAccessFineLocationApproved) {
            boolean backgroundLocationPermissionApproved =
                    androidx.core.app.ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            == android.content.pm.PackageManager.PERMISSION_GRANTED;

            if (backgroundLocationPermissionApproved) {
                // App can access location both in the foreground and in the background.
                // Start your service that doesn't have a foreground service type
                // defined.


                Radar.trackOnce(new io.radar.sdk.Radar.RadarCallback() {
                    public void onComplete(io.radar.sdk.Radar.RadarStatus status,
                                           android.location.Location location, io.radar.sdk.model.RadarEvent[]
                                                   events, io.radar.sdk.model.RadarUser user) {
                        // do something with status, location, events, user
                    }
                });
                Radar.startTracking();


            } else {
                // App can only access location in the foreground. Display a dialog
                // warning the user that your app must have all-the-time access to
                // location in order to function properly. Then, request background
                // location.

            }
        } else {
            // App doesn't have access to the device's location at all. Make full request
            // for permission.

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
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
