package cz.cvut.fit.pinadani.cardgamear.mvp.presenter;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.interactors.ISPInteractor;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IMainMenuView;
import cz.cvut.fit.pinadani.cardgamear.ui.activity.BaseFragmentActivity;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.LoginFragment;
import cz.cvut.fit.pinadani.cardgamear.utils.App;

/**
 * TODO popis
 **/
public class MainMenuPresenter extends BasePresenter<IMainMenuView> {
    public static final String TAG = MainMenuPresenter.class.getName();
    @Inject
    ISPInteractor mSpInteractor;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private boolean mLogged = false;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            mLogged = user != null;
            if (getView() != null) {
                getView().setLoginButtonText(mLogged ? R.string.logout : R.string.login);
                getView().showProfileButton(mLogged);
            }
            if (mLogged) {
                mSpInteractor.setUserEmail(user.getEmail());
            } else {
                mSpInteractor.clearAll();
            }
        };
    }

    @Override
    protected void onTakeView(IMainMenuView mainView) {
        super.onTakeView(mainView);
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onDropView() {
        super.onDropView();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onLoginLogoutClicked() {
        if (mLogged) {
            mAuth.signOut();
        } else {
            BaseFragmentActivity.startActivity(getView().getFragmentActivity(), LoginFragment.class.getName
                    ());
        }
    }
}
