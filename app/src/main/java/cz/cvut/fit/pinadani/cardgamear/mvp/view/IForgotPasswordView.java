package cz.cvut.fit.pinadani.cardgamear.mvp.view;

/**
 * View of forgot password screen
 * Created by Daniel Pina
 **/
public interface IForgotPasswordView extends IBaseView {

    void showProgress(boolean b);

    void showValidFailEmpty();

    void showValidFailEmail();
}
