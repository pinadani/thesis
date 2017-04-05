package cz.cvut.fit.pinadani.cardgamear.mvp.presenter;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IForgotPasswordView;
import cz.cvut.fit.pinadani.cardgamear.utils.App;

/**
 * Presenter for get new password logic
 * Created by daniel.pina@ackee.cz
 **/
public class ForgotPasswordPresenter extends BasePresenter<IForgotPasswordView> {
    public static final String TAG = ForgotPasswordPresenter.class.getName();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        App.getAppComponent().inject(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onTakeView(IForgotPasswordView iForgotPasswordView) {
        super.onTakeView(iForgotPasswordView);
    }

    public void sendEmail(String email) {
        if (!isValid(email)) {
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(getView().getFragmentActivity(), task -> {
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "resetPassword:failed", task.getException());
                        try {
                            throw task.getException();
                        } catch (FirebaseAuthInvalidUserException e) {
                            getView().showSnackbar(R.string.reset_password_fail_invalid_user);
                        } catch (Exception e) {
                            getView().showSnackbar(R.string.reset_password_fail);
                        }
                    } else {
                        getView().showSnackbar(R.string.reset_password_succes);
                    }
                    getView().showProgress(false);
                });

        getView().showProgress(true);
    }

    private boolean isValid(String email) {
        if (TextUtils.isEmpty(email)) {
            onValidErrorEmpty();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onValidErrorEmail();
            return false;
        }
        return true;
    }

    private void onValidErrorEmail() {
        if (getView() != null) {
            getView().showValidFailEmail();
        }
    }

    private void onValidErrorEmpty() {
        if (getView() != null) {
            getView().showValidFailEmpty();
        }
    }
}
