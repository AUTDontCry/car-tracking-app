package at.wifi.swdev.cartrackingapp.helpers;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import at.wifi.swdev.cartrackingapp.R;

public class UiHelper{

    public boolean isLocationPermissionGranted(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isLocationProviderEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean isPLayServicesAvailable(Context context) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int status = apiAvailability.isGooglePlayServicesAvailable(context);

        return ConnectionResult.SUCCESS == status;
    }

    public void showEnableLocationProviderDialog(Context context, String title, String content, boolean cancelable, String positiveText, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(content)
                .setCancelable(cancelable)
                .setPositiveButton(positiveText, listener)
                .setNegativeButton("Nein", null)
                .setIcon(R.drawable.ic_baseline_my_location_24).show();
    }
}
