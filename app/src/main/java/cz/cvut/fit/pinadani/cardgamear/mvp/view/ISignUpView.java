package cz.cvut.fit.pinadani.cardgamear.mvp.view;

/**
 * TODO add class description
 **/
public interface ISignUpView extends IBaseView {

    void signedUp();

    void showValidFailEmail();

    void showValidFailEmpty();

    void showValidFailEmptyPassword();
}
