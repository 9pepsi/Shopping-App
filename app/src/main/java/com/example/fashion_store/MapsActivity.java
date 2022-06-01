package com.example.fashion_store;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.example.fashion_store.databinding.ActivityMapsBinding;
import com.tomtom.online.sdk.common.location.LatLng;
import com.tomtom.online.sdk.map.CameraPosition;
import com.tomtom.online.sdk.map.MapFragment;
import com.tomtom.online.sdk.map.MarkerBuilder;
import com.tomtom.online.sdk.map.SimpleMarkerBalloon;
import com.tomtom.online.sdk.map.TomtomMap;

import java.util.List;

public class MapsActivity extends FragmentActivity implements com.tomtom.online.sdk.map.OnMapReadyCallback {

    private TomtomMap map;
    private ActivityMapsBinding binding;

    String userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent getAddress = getIntent();
        userAddress = getAddress.getStringExtra("address");

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(mapFragment != null) {
            mapFragment.getAsyncMap(this);
        }
    }

    @Override
    public void onMapReady(@NonNull TomtomMap tomtomMap) {
        this.map = tomtomMap;

        // Add a marker in address and move the camera
        LatLng shippingAddress = getLocationFromAddress(this, userAddress);
        SimpleMarkerBalloon balloon = new SimpleMarkerBalloon(userAddress);
        map.addMarker(new MarkerBuilder(shippingAddress).markerBalloon(balloon));
        map.centerOn(CameraPosition.builder().focusPosition(shippingAddress).zoom(15.0).build());
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);

            if(address==null) {return null;}

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return p1;
    }
}