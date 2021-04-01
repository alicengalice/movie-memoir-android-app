package com.example.mymoviememoir;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng pAddress;
    private LatLng cAddress;
    private HashMap<String, LatLng> cinemaListLatlng;
    private String cinemaname;

    private ArrayList<String> strCinemaListLatlng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        pAddress = null;
        cinemaListLatlng = null;
        cAddress = null;
        cinemaname = "";

        Intent intent = getIntent();
        String location = intent.getStringExtra("pAddress");
        String[] latlong= location.split(",");
        double latitude = Double.parseDouble(latlong[0]);
        double longitude = Double.parseDouble(latlong[1]);
        pAddress = new LatLng(latitude, longitude);


        strCinemaListLatlng = (ArrayList<String>) getIntent().getSerializableExtra("cinemaListLatlng");
       /* for (int i = 0; i < strCinemaListLatlng.size(); i++) {
            String cinema = strCinemaListLatlng.get(i).toString();
            String[] parts = cinema.split("&");
            String cinemaid = parts[0];
            cinemaname = parts[1];
            String postcode = parts[2];

            String[] cinemalatlong= postcode.split(",");
            double cinemalatitude = Double.parseDouble(cinemalatlong[0]);
            double cinemalongitude = Double.parseDouble(cinemalatlong[1]);
            cAddress = new LatLng(cinemalatitude, cinemalongitude);
            cinemaListLatlng.put("Latlng", cAddress);
        }*/


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        mMap.addMarker(new MarkerOptions().position(pAddress).title("My location"));

        for (int i = 0; i < strCinemaListLatlng.size(); i++) {
            try {
                String cinema = strCinemaListLatlng.get(i).toString();
                String[] parts = cinema.split("&");
                String cinemaid = parts[0];
                cinemaname = parts[1];
                String postcode = parts[2];

                String[] cinemalatlong = postcode.split(",");
                double cinemalatitude = Double.parseDouble(cinemalatlong[0]);
                double cinemalongitude = Double.parseDouble(cinemalatlong[1]);
                cAddress = new LatLng(cinemalatitude, cinemalongitude);
                mMap.addMarker(new MarkerOptions().position(cAddress).title(cinemaname).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                float zoomLevel = (float) 15.0;
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cAddress, zoomLevel));
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }
        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentlocation));
             /*Zoom levels
             1: World
             5: Landmass/continent
             10: City
             15: Streets
             20: Buildings
             */
        float zoomLevel = (float) 10.0;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pAddress,
                zoomLevel));

        /*mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }
}
