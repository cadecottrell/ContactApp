package com.example.contact;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


//This Activity displays the Google Map. It shows the Contact's address on the map, shows the Contacts Lat and Long
// It also shows the Users lat and long and calculates the difference between those.
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<String> address;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        //Save the address
        address = intent.getStringArrayListExtra("address");

        geocoder = new Geocoder(MapsActivity.this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Gets the Address
        String addressLine = address.get(0) + ", " + address.get(1) + ", " + address.get(2) + ", " + address.get(3) + ", " + address.get(4);
        try
        {
            //Looks for the address
            List<Address> list = geocoder.getFromLocationName(addressLine, 5);

            //If we found the address show it
            if(list.size() > 0)
            {
                Address address = list.get(0);


                //The potential error is handled in MainMenu by getting permissions.
                LocationManager lm = (LocationManager)this.getSystemService(this.LOCATION_SERVICE);
                Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


                double longitude = location.getLongitude();
                double latitude = location.getLatitude();

                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                //Set up custom InfoWindow
                mMap.setInfoWindowAdapter(new InfoWindowAdapter(MapsActivity.this));

                //Calculation of the path (Not following any roads) in Meters
                float[] results = new float[1];
                Location.distanceBetween(latitude, longitude, latLng.latitude, latLng.longitude, results);

                //converts Meters to miles
                double answer = (double) results[0] * 0.000621;

                //Add to map
                mMap.addMarker(new MarkerOptions().position(latLng).title("Contact Address").snippet("Address: "+ addressLine +"\nLat: " + latLng.latitude + "\nLong: " + latLng.longitude +
                        "\n" +"Your Location: " + latitude + "\n" + longitude + "\nDistance Away: " + answer + " miles"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            }
            else
            {
                //If cant find address display toast.
                Toast.makeText(this, "Address could not be found", Toast.LENGTH_LONG).show();
            }
        }
        catch (IOException ex)
        {
            System.out.println(ex);
        }
    }


}
