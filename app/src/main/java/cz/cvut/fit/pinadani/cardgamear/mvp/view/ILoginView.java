package cz.cvut.fit.pinadani.cardgamear.mvp.view;

/**
 * View of login screen
 **/
public interface ILoginView extends IBaseView {

    void showLoginFail();

    void onValidErrorEmptyEmail();

    void onValidErrorEmptyPassword();

    void onValidErrorInvalidEmail();

    void showInvalidEmailOrPassword();
}
