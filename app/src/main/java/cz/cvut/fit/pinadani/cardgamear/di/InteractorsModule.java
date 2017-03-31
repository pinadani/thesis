package cz.cvut.fit.pinadani.cardgamear.di;

import android.app.Application;

import javax.inject.Singleton;

import cz.cvut.fit.pinadani.cardgamear.interactors.ISPInteractor;
import cz.cvut.fit.pinadani.cardgamear.interactors.SPInteractorImpl;
import dagger.Module;
import dagger.Provides;

/**
 * Module that handles injecting Interactors
 * Created on {2. 4. 2015}
 */
@Module(
)
public class InteractorsModule {
    public static final String TAG = InteractorsModule.class.getName();

    @Provides
    @Singleton
    public ISPInteractor provideSPInteractor(Application app) {
        return new SPInteractorImpl(app.getApplicationContext());
    }
}
