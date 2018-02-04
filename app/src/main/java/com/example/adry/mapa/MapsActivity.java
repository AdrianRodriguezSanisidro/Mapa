package com.example.adry.mapa;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private static final String TAG="gpslog";
    private LocationManager mLocMgr;
    private TextView textViewGPS,textViewDist;
    private Button qr;

    private LatLng tesoro;
    private Location tesoroLoc;
    private double lat=42.263044,lng=-8.802236;
    private static final long MIN_CAMBIO_DISTANCIA=(long) 5;
    private static final long MIN_TIEMPO_UPDATE=1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment=(SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.e(TAG, "No se tienen permisos necesarios!");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
            return;
    }else{
        Log.i(TAG, "Permisos necesarios OK!");
        mLocMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIEMPO_UPDATE, MIN_CAMBIO_DISTANCIA, locListener, Looper.getMainLooper());
    }
    textViewGPS.setText("Lat "+ "Long ");


    tesoro=new LatLng(lat,lng);
    tesoroLoc=new Location(LocationManager.GPS_PROVIDER);
    tesoroLoc.setLatitude(lat);
    tesoroLoc.setLongitude(lng);

    }
    public LatLng miPosicion;
    public LocationListener locListener= new LocationListener(){

        @Override
        public void onLocationChanged(Location location) {
            Log.v(TAG,"Lat " + location.getLatitude()+ " Long " + location.getLongitude());
            miPosicion= new LatLng(location.getLatitude(),location.getLongitude());
        }


        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.i(TAG,"onProviderDisabled()");
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.i(TAG,"onProviderEnabled()");
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.i(TAG, "onProviderEnabled()");
        }
    };


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

        mMap.getUiSettings().setAllGesturesEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);




        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        LatLng vigo = new LatLng(42.236341691474884, -8.714316040277481);
        mMap.addMarker(new MarkerOptions().position(tesoro).title("Marca de Tesoro"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vigo,16.5F));


        qr = (Button)findViewById(R.id.irAlQR);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pantallaQR=new Intent(MapsActivity.this,QRActivity.class);
            }
        });

        Circle circle = mMap.addCircle(new CircleOptions()
                .center(vigo)
                .radius(250)
                .strokeColor(Color.RED));


    }
}
