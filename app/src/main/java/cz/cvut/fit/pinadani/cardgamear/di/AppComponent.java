package cz.cvut.fit.pinadani.cardgamear.di;

import javax.inject.Singleton;

import cz.cvut.fit.pinadani.cardgamear.ar.libgdx.Display;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ChangePasswordPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ConnectionPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ForgotPasswordPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.LoginPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.MainMenuPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ProfilePresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.SignUpPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.StatisticsPresenter;
import cz.cvut.fit.pinadani.cardgamear.service.BluetoothService;
import dagger.Component;

/**
 * Main application component
 * Created on {23. 6. 2015}
 **/
@Singleton
@Component(modules = {
        AppModule.class})
public interface AppComponent {

    void inject(LoginPresenter loginPresenter);

    void inject(SignUpPresenter signUpPresenter);

    void inject(ForgotPasswordPresenter forgotPasswordPresenter);

    void inject(MainMenuPresenter mainMenuPresenter);

    void inject(ConnectionPresenter connectionPresenter);

    void inject(ProfilePresenter profilePresenter);

    void inject(ChangePasswordPresenter changePasswordPresenter);

    void inject(StatisticsPresenter statisticsPresenter);

    void inject(Display display);

    void inject(BluetoothService bluetoothService);
}
