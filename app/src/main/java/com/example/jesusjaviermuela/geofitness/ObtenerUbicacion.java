package com.example.jesusjaviermuela.geofitness;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Jesus Javier Muela on 13/02/2018.
 */

public class ObtenerUbicacion extends AsyncTask {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private double latitud;
    private double longitud;
    private boolean finalizado;

    public ObtenerUbicacion(LocationManager locationManager, LocationListener locationListener) {
        this.locationManager = locationManager;
        this.locationListener = locationListener;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    @Override
    protected Object doInBackground(Object[] objects) {


        return null;
    }
}
