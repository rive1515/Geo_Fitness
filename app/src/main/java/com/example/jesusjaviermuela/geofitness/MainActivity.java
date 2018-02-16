package com.example.jesusjaviermuela.geofitness;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.commons.ServicesException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LocationListener locationListener;
    LocationManager locationManager;
    double latitud, latitudInicial;
    double longitud, longitudInicial;
    Location localizacionInicial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onLocationChanged(Location location) {
                Location origen = new Location(LocationManager.GPS_PROVIDER);
                origen.setLatitude(latitudInicial);
                origen.setLongitude(longitudInicial);
                MapsActivity mapsActivity = new MapsActivity();
                try {
                    mapsActivity.obtenerRuta(origen , location);
                } catch (ServicesException e) {
                    e.printStackTrace();
                }

                System.out.println(location.toString());
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

        if(Build.VERSION.SDK_INT >= 23){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                //si no lo tenemos lo pedimos
                ActivityCompat.requestPermissions(this,
                        new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }

        }

        ArrayList listaProveedores = (ArrayList) locationManager.getProviders(true);
        int x = 0;
        while (listaProveedores.size() > x){
            System.out.println("PROVEEDOR: "+listaProveedores.get(x).toString());
            x++;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, locationListener);

        while(localizacionInicial == null){

            localizacionInicial = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //  ultimaLocalizacion = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (localizacionInicial != null){
                latitudInicial =  localizacionInicial.getLatitude();
                longitudInicial = localizacionInicial.getLongitude();
            }
        }


        Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
        mapsIntent.putExtra("latitud", latitudInicial);
        mapsIntent.putExtra("longitud", longitudInicial);
        startActivity(mapsIntent);
    }

}
