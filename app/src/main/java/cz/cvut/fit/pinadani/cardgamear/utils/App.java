package cz.cvut.fit.pinadani.cardgamear.utils;

import android.app.Application;
import android.content.Context;

/**
 * TODO add class description
 * Created by JMENO_PRIJMENI[jmeno.prijmeni@ackee.cz] on {2/22/2017}
 **/
public class App extends Application {
    public static final String TAG = App.class.getName();
    private static App sInstance;

    public App() {
        sInstance = this;
    }

    public static Context getContext() {
        return sInstance;
    }
}
