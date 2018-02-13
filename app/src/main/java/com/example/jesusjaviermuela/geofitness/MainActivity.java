package com.example.jesusjaviermuela.geofitness;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.services.commons.ServicesException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LocationListener locationListener;
    LocationManager locationManager;
    double latitud;
    double longitud;
    Location ultimaLocalizacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if(Build.VERSION.SDK_INT >= 23){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                //si no lo tenemos lo pedimos
                ActivityCompat.requestPermissions(this,
                        new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }

        }

        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        ArrayList listaProveedores = (ArrayList) locationManager.getProviders(true);
        int x = 0;
        while (listaProveedores.size() > x){
            System.out.println("PROVEEDOR: "+listaProveedores.get(x).toString());
            x++;
        }

        while(ultimaLocalizacion == null){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, locationListener);
            ultimaLocalizacion = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //  ultimaLocalizacion = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (ultimaLocalizacion != null){
                latitud =  ultimaLocalizacion.getLatitude();
                longitud = ultimaLocalizacion.getLongitude();
            }
        }


        Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
        mapsIntent.putExtra("latitud", latitud);
        mapsIntent.putExtra("longitud", longitud);
        startActivity(mapsIntent);
    }
}
