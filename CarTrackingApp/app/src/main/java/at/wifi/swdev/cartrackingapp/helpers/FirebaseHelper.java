package at.wifi.swdev.cartrackingapp.helpers;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import at.wifi.swdev.cartrackingapp.models.Driver;

public class FirebaseHelper {

    public static final String ONLINE_DRIVERS = "online_drivers";
    private String driverID;
    private DatabaseReference databaseReference;

    public FirebaseHelper(String driverID) {
        this.driverID = driverID;
        databaseReference = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(ONLINE_DRIVERS)
                .child(driverID);

        databaseReference.onDisconnect().removeValue();
    }

    public void updateDriver(Driver driver) {
        databaseReference.setValue(driver);
    }

    public void deleteDriver() {
        databaseReference.removeValue();
    }
}
