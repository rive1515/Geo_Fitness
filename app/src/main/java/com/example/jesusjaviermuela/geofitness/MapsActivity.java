package com.example.jesusjaviermuela.geofitness;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;


import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationServices;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
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


public class MapsActivity extends FragmentActivity{
    private MapView mapaView;
    private MapboxMap mapa;
    private FloatingActionButton btUbicacion;
    private LocationServices servicioUbicacion;
    private double latitud;
    private double longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MapboxAccountManager.start(this, "pk.eyJ1Ijoicml2ZTE1MTUiLCJhIjoiY2pkbHV0M3I0MGNnYjJ3amd5OTNhdTBzeSJ9.zAdn6OsJ0nDdampdV50TGw");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    /*    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        latitud = intent.getDoubleExtra("latitud",0);
        longitud = intent.getDoubleExtra("longitud", 0);

*/
        mapaView = (MapView) findViewById(R.id.mapaView);
        mapaView.onCreate(savedInstanceState);



        Intent intent = getIntent();
        latitud = intent.getDoubleExtra("latitud",0);
        longitud = intent.getDoubleExtra("longitud", 0);

        mapaView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mapa = mapboxMap;
                LatLng origen = new LatLng(latitud, longitud);
                LatLng destino = new LatLng(39.387133, -3.216995);


                try {
                    obtenerRuta(origen, destino);
                } catch (Exception ex) {

                }
            }
        });
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


    public void obtenerRuta(LatLng origen, LatLng destino) throws ServicesException {

        Position posicionOrigen = Position.fromCoordinates(origen.getLongitude(), origen.getLatitude());
        Position posicionDestino = Position.fromCoordinates(destino.getLongitude(),destino.getLatitude());

        MapboxDirections direccion = new MapboxDirections.Builder()
                .setOrigin(posicionOrigen)
                .setDestination(posicionDestino)
                .setProfile(DirectionsCriteria.PROFILE_DRIVING)
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

        mapa.addPolyline(new PolylineOptions().add(puntos)
                .color(Color.parseColor("#009688")).width(5));

        mapa.setMyLocationEnabled(true);
        if(!mapa.isMyLocationEnabled()){
            mapa.setMyLocationEnabled(true);
        }
    }
}
