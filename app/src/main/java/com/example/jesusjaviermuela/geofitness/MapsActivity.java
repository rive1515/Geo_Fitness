package com.example.jesusjaviermuela.geofitness;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.services.Constants;
import com.mapbox.services.commons.ServicesException;
import com.mapbox.services.commons.geojson.LineString;
import com.mapbox.services.commons.models.Position;
import com.mapbox.services.directions.v5.DirectionsCriteria;
import com.mapbox.services.directions.v5.MapboxDirections;
import com.mapbox.services.directions.v5.models.DirectionsResponse;
import com.mapbox.services.directions.v5.models.DirectionsRoute;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private double latitud;
    private double longitud;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapboxAccountManager.start(this, "pk.eyJ1Ijoicml2ZTE1MTUiLCJhIjoiY2pkbG0wODF6MGE2dDJxcWk1NW5mbGt4OSJ9.k2d6hhc4cczzz6ynmdLO6Q");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        latitud = intent.getDoubleExtra("latitud",0);
        longitud = intent.getDoubleExtra("longitud", 0);



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

//        // Add a marker in Sydney and move the camera
        LatLng origen = new LatLng(latitud, longitud);
        LatLng destino = new LatLng(39.387133, -3.216995);
        mMap.addMarker(new MarkerOptions().position(origen).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origen));

        try {
            obtenerRuta(origen, destino);
        } catch (ServicesException e) {
            e.printStackTrace();
        }
    }


    public void obtenerRuta(LatLng origen, LatLng destino) throws ServicesException {

        Position posicionOrigen = Position.fromCoordinates(origen.longitude, origen.latitude);
        Position posicionDestino = Position.fromCoordinates(destino.longitude,destino.latitude);

        MapboxDirections direccion = new MapboxDirections.Builder()
                .setOrigin(posicionOrigen)
                .setDestination(posicionDestino)
                .setProfile(DirectionsCriteria.PROFILE_WALKING)
                .setAccessToken(MapboxAccountManager.getInstance().getAccessToken())
                .build();

        direccion.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                DirectionsRoute ruta = response.body().getRoutes().get(0);
                Toast.makeText(MapsActivity.this, "Distancia: " + ruta.getDistance() + " metros", Toast.LENGTH_SHORT).show();

                pintarRuta(ruta);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });




    }

    @SuppressLint("MissingPermission")
    private void pintarRuta (DirectionsRoute ruta){
        //Recogemos los puntos de ruta
        LineString lineString = LineString.fromPolyline(ruta.getGeometry(), Constants.OSRM_PRECISION_V5);
        List<Position> coordenadas = lineString.getCoordinates();
        LatLng [] puntos = new LatLng[coordenadas.size()];

        for (int i = 0; i <coordenadas.size(); i++){
            puntos[i] = new LatLng(coordenadas.get(i).getLatitude(), coordenadas.get(i).getLongitude());
        }

        mMap.addPolyline(new PolylineOptions().add(puntos)
                .color(Color.parseColor("#009688")).width(5));

        if(!mMap.isMyLocationEnabled()){
            mMap.setMyLocationEnabled(true);
        }
    }
}
