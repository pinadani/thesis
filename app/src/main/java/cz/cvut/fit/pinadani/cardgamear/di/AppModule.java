package cz.cvut.fit.pinadani.cardgamear.di;

import android.app.Application;
import android.view.LayoutInflater;

import javax.inject.Singleton;

import cz.cvut.fit.pinadani.cardgamear.utils.App;
import dagger.Module;
import dagger.Provides;

/**
 * Module that handle injecting application class (ie Context)
 * Created on {2. 4. 2015}
 */
@Module(
        includes = {
                InteractorsModule.class,
        }
)
public class AppModule {
    public static final String TAG = AppModule.class.getName();
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    LayoutInflater provideLayoutInflater() {
        return LayoutInflater.from(app);
    }
}
