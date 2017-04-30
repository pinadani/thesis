package cz.cvut.fit.pinadani.cardgamear.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Network utilities
 * Created on {18/12/15}
 **/
public class NetworkUtils {
    public static final String TAG = NetworkUtils.class.getName();

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] activeNetworkInfos = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo networkInfo : activeNetworkInfos) {
            if (networkInfo.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }
}
