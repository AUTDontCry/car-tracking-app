package at.wifi.swdev.cartrackingapp.animation;

import com.google.android.gms.maps.model.LatLng;

public interface LatLngInterpolator {

    LatLng interpolate(float fraction, LatLng from, LatLng to);

}
