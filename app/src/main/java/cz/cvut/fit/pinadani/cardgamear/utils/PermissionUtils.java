package cz.cvut.fit.pinadani.cardgamear.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Utility class for permissions
 * Created by Jan Stanek[jan.stanek@ackee.cz] on {27.2.16}
 **/
public class PermissionUtils {
    public static final String TAG = PermissionUtils.class.getName();

    public static boolean areLocationPermissionsGranted(Context ctx) {
        String accessFineLocation = Manifest.permission.ACCESS_FINE_LOCATION;
        return ActivityCompat.checkSelfPermission(ctx, accessFineLocation) == PackageManager.PERMISSION_GRANTED;
    }
}
