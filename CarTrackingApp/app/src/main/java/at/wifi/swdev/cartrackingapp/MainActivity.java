package at.wifi.swdev.cartrackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import at.wifi.swdev.cartrackingapp.animation.SphericalInterpolator;
import at.wifi.swdev.cartrackingapp.helpers.FirebaseHelper;
import at.wifi.swdev.cartrackingapp.helpers.GoogleMapHelper;
import at.wifi.swdev.cartrackingapp.helpers.MarkerAnimationHelper;
import at.wifi.swdev.cartrackingapp.helpers.UiHelper;
import at.wifi.swdev.cartrackingapp.models.Driver;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private boolean driverOnline;
    private final UiHelper uiHelper = new UiHelper();
    private final FirebaseHelper firebaseHelper = new FirebaseHelper("SWDEV-001");
    private final MarkerAnimationHelper markerAnimationHelper = new MarkerAnimationHelper();
    private final GoogleMapHelper googleMapHelper = new GoogleMapHelper();
    public static final int REQUEST_LOCATION_PERMISSION_CODE = 1;
    private boolean animateCamera = true;
    private GoogleMap googleMap;
    private Marker currentPositionMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        driverOnline = false;

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.supportMap);
        supportMapFragment.getMapAsync(gm -> googleMap = gm);

        createLocationCallback();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(3000);

        if (uiHelper.isPLayServicesAvailable(this)) {
            requestLocationUpdate();
        } else {
            Toast.makeText(this, "Google PlayServices sind nicht verfügbar.", Toast.LENGTH_LONG).show();
            finish();
        }

    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (locationResult.getLastLocation() == null) {
                    return;
                }

                LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                Log.d("Location", latLng.latitude + "," + latLng.longitude);

                if (animateCamera) {
                    animateCamera = false;
                    animateCamera(latLng);
                }

                if (driverOnline) {
                    firebaseHelper.updateDriver(new Driver("SWDEV-001", latLng.latitude, latLng.longitude));
                }
                showOrAnimateMarker(latLng);



            }

        };
    }

    private void showOrAnimateMarker(LatLng latLng) {
        if (currentPositionMarker != null) {
            markerAnimationHelper.animateMarkerTo(currentPositionMarker, latLng, new SphericalInterpolator());
        } else {
            currentPositionMarker = googleMap.addMarker(googleMapHelper.getDriverMarkerOptions(latLng));
        }
    }

    private void animateCamera(LatLng latLng) {
        CameraUpdate cameraUpdate = googleMapHelper.buildCameraUpdate(latLng);
        googleMap.animateCamera(cameraUpdate, 10, null);
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate() {
        if (!uiHelper.isLocationPermissionGranted(this)) {
            final String[] permissions = {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            };

            ActivityCompat.requestPermissions(this, permissions, REQUEST_LOCATION_PERMISSION_CODE);
                  return;
            }

        if (!uiHelper.isLocationProviderEnabled(this)) {
            uiHelper.showEnableLocationProviderDialog(
                    this,
                    "Standort aktivieren",
                    "Bitte den Standort in den Einstellungen aktivieren",
                    true,
                    "Aktivieren",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });
        }

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu, menu);
            return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdate();
            } else {
                Toast.makeText(this, "App funktioniert ohne Standort-Berechtingung nicht.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
       if (item.getItemId() == R.id.driver_status) {
           if (!driverOnline){
               driverOnline = true;
               item.setIcon(R.drawable.ic_baseline_my_location_24);
               item.setTitle("Online");
           }
           else{
               driverOnline = false;
               item.setIcon(R.drawable.ic_baseline_location_searching_24);
               item.setTitle("Offline");
               firebaseHelper.deleteDriver();
           }

        }
        return true;
    }
    }