package at.wifi.swdev.cartrackingapp.animation;

import com.google.android.gms.maps.model.LatLng;

public class SphericalInterpolator implements LatLngInterpolator {



    @Override
    public LatLng interpolate(float fraction, LatLng a, LatLng b) {
        // http://en.wikipedia.org/wiki/Slerp
        double fromLat = Math.toRadians(a.latitude);
        double fromLng = Math.toRadians(a.longitude);
        double toLat = Math.toRadians(b.latitude);
        double toLng = Math.toRadians(b.longitude);
        double cosFromLat = Math.cos(fromLat);
        double cosToLat = Math.cos(toLat);

        // Computes Spherical interpolation coefficients.
        double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
        double sinAngle = Math.sin(angle);

        if (sinAngle < 1E-6) {
            return a;
        }

        double temp1 = Math.sin((1 - fraction) * angle) / sinAngle;
        double temp2 = Math.sin(fraction * angle) / sinAngle;

        // Converts from polar to vector and interpolate.
        double x = temp1 * cosFromLat * Math.cos(fromLng) + temp2 * cosToLat * Math.cos(toLng);
        double y = temp1 * cosFromLat * Math.sin(fromLng) + temp2 * cosToLat * Math.sin(toLng);
        double z = temp1 * Math.sin(fromLat) + temp2 * Math.sin(toLat);

        // Converts interpolated vector back to polar.
        double lat = Math.atan2(z, Math.sqrt(x * x + y * y));
        double lng = Math.atan2(y, x);

        return new LatLng(Math.toDegrees(lat), Math.toDegrees(lng));
    }

    private double computeAngleBetween(double fromLat, double fromLng, double toLat, double toLng) {
        double dLat = fromLat - toLat;
        double dLng = fromLng - toLng;

        return 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(dLat / 2), 2.0) + Math.cos(fromLat) * Math.cos(toLat) * Math.pow(Math.sin(dLng / 2), 2.0)));
    }

}
