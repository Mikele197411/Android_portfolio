package com.mshilkov.applocation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {

    Button startLocationUpdate;
    Button stopLoactionUpdate;
    TextView locationTextView;
    FusedLocationProviderClient fusedLocationProviderClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startLocationUpdate=findViewById(R.id.startLocationUpdates);
        stopLoactionUpdate=findViewById(R.id.stopLocationUpdates);
        locationTextView=findViewById(R.id.locationTextView);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }
}