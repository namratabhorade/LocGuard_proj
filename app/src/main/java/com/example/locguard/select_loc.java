package com.example.locguard;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;



public class select_loc extends FragmentActivity implements OnMapReadyCallback,
        LocationListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    LocationManager mLocationManager;
    private LocationManager locationManager;
    private boolean isPermission;
    SearchView searchView;
    String TAG = select_loc.class.getSimpleName();
    LatLng latLng1;
    LatLng latLng;
    AudioManager am;
    double current_lattitude, current_longitude, new_lattitude, new_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_loc);
        searchView=findViewById(R.id.location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if(requestSinglePermission())
        {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
          //  SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
           // mapFragment.getMapAsync(this);

            mGoogleApiClient=new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();
            mLocationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            checkLocation();

        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }
        am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(select_loc.this);
                    try
                    {
                       addressList = geocoder.getFromLocationName(location, 1);
                      Log.i(TAG,"In try block");

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                    Address address = addressList.get(0);
                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    new_lattitude=address.getLatitude();
                    new_longitude=address.getLongitude();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    Toast.makeText(getApplicationContext(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();




                }
                Test t=new Test();
                final Thread th=new Thread(t);
                th.start();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }



        });
        mapFragment.getMapAsync(this);


    }
    private boolean checkLocation() {
        if(!isLocationEnabled())
        {
            showAlert();
        }
        return isLocationEnabled();
    }

    private void showAlert()
    {
        final AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Location setting is set to OFF.\nPlease Enable location to"+"use this app")
                .setPositiveButton("Location Setting ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();
    }

    private boolean isLocationEnabled()
    {
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return /*locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||*/
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

    }

    private boolean requestSinglePermission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                isPermission=true;
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

                if(response.isPermanentlyDenied())
                {
                    isPermission=false;
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {


            }
        }).check();


        return isPermission;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle)
    {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(50);
        mLocationRequest.setFastestInterval(50);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null)
        {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        /*
       latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
        current_lattitude=location.getLatitude();
        current_longitude=location.getLongitude();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng1);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        /*if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }*/
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    class Test implements Runnable{

        public  void run()
        {
            float[] distance = new float[1];
            while(true) {
                latLng1 = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                current_lattitude = mLastLocation.getLatitude();
                current_longitude = mLastLocation.getLongitude();
                Location.distanceBetween(new_lattitude, new_longitude, current_lattitude, current_longitude, distance);
                if (distance[0] <= 500) {
                    am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                    break;

                }
            }
            while(true)
            {
                latLng1 = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                current_lattitude = mLastLocation.getLatitude();
                current_longitude = mLastLocation.getLongitude();
               // Toast.makeText(getApplicationContext(),current_lattitude+""+current_longitude,Toast.LENGTH_SHORT).show();
                Location.distanceBetween(new_lattitude, new_longitude, current_lattitude, current_longitude, distance);
                if (distance[0] > 500)
                {
                    am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    break;
                }
            }




        }
    }

    /*public void searchLocation(View view) {
        EditText locationSearch =findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(location));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            Toast.makeText(getApplicationContext(),address.getLatitude()+" "+address.getLongitude(),Toast.LENGTH_LONG).show();
        }
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent int1 = new Intent(select_loc.this,Menu.class);
        startActivity(int1);
    }
}