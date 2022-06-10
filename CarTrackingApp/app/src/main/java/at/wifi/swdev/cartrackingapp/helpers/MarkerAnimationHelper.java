package at.wifi.swdev.cartrackingapp.helpers;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import at.wifi.swdev.cartrackingapp.animation.LatLngInterpolator;

public class MarkerAnimationHelper {
    public void animateMarkerTo(Marker marker, LatLng finalPosition, LatLngInterpolator latLngInterpolator) {
        LatLng startPosition = marker.getPosition();
        Handler handler = new Handler();

        float durationInMillis = 2000f;
        long start = SystemClock.uptimeMillis();

        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();

        handler.post(new Runnable() {

            long elapsed = 0;
            float t = 0;
            float v = 0;

            @Override
            public void run() {
                elapsed = SystemClock.uptimeMillis() - start;
                t = elapsed / durationInMillis;
                v = interpolator.getInterpolation(t);

                marker.setPosition(latLngInterpolator.interpolate(v, startPosition, finalPosition));

                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
}
