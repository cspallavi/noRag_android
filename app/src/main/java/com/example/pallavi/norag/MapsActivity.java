package com.example.pallavi.norag;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Intent;
import android.widget.Toast;

import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    float destinationlatitude, destinationlongitude, sourcelatitude, sourcelongitude;
    LocationManager locationManager;
    String mprovider;
    Location location;
    MaterialDialog mate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        Log.v("Check", "In the Map Activity");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        destinationlatitude = getIntent().getExtras().getFloat("latitude");
        destinationlongitude = getIntent().getExtras().getFloat("longitude");
        Log.v("Location Destination ", " " + destinationlatitude + " " + destinationlongitude);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
        sourcelatitude = sp.getFloat("sourcelatitude", -1);
        sourcelongitude = sp.getFloat("sourcelongitude", -1);
        Log.v("Location Source ", " " + sourcelatitude + " " + sourcelongitude);
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
       /* mate = new MaterialDialog.Builder(MapsActivity.this)
                .title("No-Rag")
                .content("Please Wait While Members are fetched")
                .progress(true, 0)
                .show();
        mate.setCanceledOnTouchOutside(false);
        */
        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        LatLng source = new LatLng(sourcelatitude, sourcelongitude);
        //Fix coordinate at a later stage
        //LatLng destination = new LatLng(sourcelatitude-0.007769, sourcelongitude-0.011327);
        LatLng destination = new LatLng(destinationlatitude, destinationlongitude);
        DrawRouteMaps.getInstance(this)
                .draw(source, destination, mMap);
        DrawMarker.getInstance(this).draw(mMap, source, R.drawable.marker_a, "Student Location");
        DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Authority Location");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(source)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));
//        mate.dismiss();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home=new Intent(MapsActivity.this, com.example.pallavi.norag.Introduction.class);
        home.putExtra("code",2);
        startActivity(home);
        MapsActivity.this.finish();

    }
}


