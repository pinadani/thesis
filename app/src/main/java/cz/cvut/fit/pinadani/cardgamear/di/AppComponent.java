package cz.cvut.fit.pinadani.cardgamear.di;

import javax.inject.Singleton;

import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ConnectBluetoothPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ForgotPasswordPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.LoginPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.MainMenuPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.SignUpPresenter;
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

    void inject(ConnectBluetoothPresenter connectBluetoothPresenter);
}
