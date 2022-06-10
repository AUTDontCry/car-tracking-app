package at.wifi.swdev.cartrackingapp.helpers;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import at.wifi.swdev.cartrackingapp.R;

public class GoogleMapHelper {

    public static final int ZOOM_LEVEL = 18;
    public static final int TILT_LEVEL = 25;

    public MarkerOptions getDriverMarkerOptions(LatLng latLng) {
        MarkerOptions options = getMarkerOptions(R.drawable.car_icon, latLng);
        options.flat(true);
        return options;
    }

    public MarkerOptions getMarkerOptions(int resource, LatLng latLng) {
        return new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(resource))
                .position(latLng);
    }

    public CameraUpdate buildCameraUpdate(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .tilt(TILT_LEVEL)
                .zoom(ZOOM_LEVEL)
                .build();

        return CameraUpdateFactory.newCameraPosition(cameraPosition);
    }


}
