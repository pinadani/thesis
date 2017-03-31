package cz.cvut.fit.pinadani.cardgamear.utils;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.FirebaseApp;

import cz.cvut.fit.pinadani.cardgamear.di.AppComponent;
import cz.cvut.fit.pinadani.cardgamear.di.AppModule;
import cz.cvut.fit.pinadani.cardgamear.di.DaggerAppComponent;

/**
 * TODO add class description
 * Created by JMENO_PRIJMENI[jmeno.prijmeni@ackee.cz] on {2/22/2017}
 **/
public class App extends Application {
    public static final String TAG = App.class.getName();
    private static App sInstance;
    private static AppComponent mAppComponent;

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static void setAppComponent(@NonNull AppComponent appComponent) {
        mAppComponent = appComponent;
    }
    public App() {
        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static Context getContext() {
        return sInstance;
    }
}
