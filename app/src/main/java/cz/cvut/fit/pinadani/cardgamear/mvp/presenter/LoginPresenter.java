package cz.cvut.fit.pinadani.cardgamear.mvp.presenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import javax.inject.Inject;

import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.interactors.ISPInteractor;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.ILoginView;
import cz.cvut.fit.pinadani.cardgamear.utils.App;
import cz.cvut.fit.pinadani.cardgamear.utils.SDKHelper;

/**
 * Presenter for login logic
 **/
public class LoginPresenter extends BasePresenter<ILoginView> {
    public static final String TAG = LoginPresenter.class.getName();
    @Inject
    ISPInteractor mSpInteractor;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onTakeView(ILoginView iLoginView) {
        super.onTakeView(iLoginView);
    }

    public void login(String email, String pwd) {
        email = SDKHelper.removeWhiteSpacesFromEnd(email);
        if (!validation(email, pwd)) {
            getView().showInvalidEmailOrPassword();
        } else {
            getView().showProgress(true);
            mAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnCompleteListener(getView().getFragmentActivity(), task
                            -> {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                // malo znaku hesla (slabe heslo)
                                getView().showSnackbar(R.string.login_fail_week_password);
                            } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException e) {
                                getView().showSnackbar(R.string.login_fail_invalid_password_or_email);
                            } catch (Exception e) {
                                getView().showSnackbar(R.string.login_fail);
                            }


                        } else {
                            mSpInteractor.setUserEmail(task.getResult().getUser().getEmail());
                            if (getView() != null) {
                                getView().showSnackbar(R.string.login_success);
                                getView().getFragmentActivity().finish();
                            }
                        }
                        if (getView() != null) {
                            getView().showProgress(false);
                        }
                    });
        }
    }

    private boolean validation(String email, String pwd) {
        boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            if (getView() != null) {
                getView().onValidErrorEmptyEmail();
            }
            valid = false;
        }
        if (TextUtils.isEmpty(pwd)) {
            if (getView() != null) {
                getView().onValidErrorEmptyPassword();
            }
            valid = false;
        }
        if (!isEmailValid(email)) {
            if (getView() != null) {
                getView().onValidErrorInvalidEmail();
            }
            valid = false;
        }
        return valid;
    }


    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
