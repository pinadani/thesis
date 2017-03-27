package cz.cvut.fit.pinadani.cardgamear.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.ui.activity.BaseActivity;
import cz.cvut.fit.pinadani.cardgamear.utils.SDKHelper;


public class LoginFragment extends BaseFragment  {
    public static final String TAG = LoginFragment.class.getName();

    @Bind(R.id.edit_email)
    EditText mUsernameEditText;
    @Bind(R.id.edit_password)
    EditText mPasswordEditText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
            }
        };

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @OnClick(R.id.btn_login)
    public void onLoginButtonClicked() {
        startLoginTask();
    }

    @OnClick(R.id.btn_forgot_password)
    public void onForgotPasswordClicked() {
        BaseActivity.startActivity(getActivity(), ForgotPasswordFragment.class.getName());
    }

    @OnClick(R.id.btn_registration)
    public void onRegistrationClicked() {
        BaseActivity.startActivity(getActivity(), RegistrationFragment.class.getName());
    }

    /**
     * Spusteni login tasku
     *
     */
    private void startLoginTask() {
        String email = mUsernameEditText.getText().toString();
        email = SDKHelper.removeWhiteSpacesFromEnd(email);
        String password = mPasswordEditText.getText().toString();

        if (!validation(email, password)) {
            return;
        } else {
            showProgressDialog(getFragmentManager(), false);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
                                showSnackbar(R.string.login_fail);
                            }

                            dismissProgressDialog();
                        }
                    });
        }
    }


    private boolean validation(String email, String password) {
        boolean valid = true;
        if (TextUtils.isEmpty(email)) {
            onValidErrorEmptyEmail();
            valid = false;
        }
        if (TextUtils.isEmpty(password)) {
            onValidErrorEmptyPassword();
            valid = false;
        }
        if (!SDKHelper.isEmailValid(email)) {
            onValidErrorInvalidEmail();
            valid = false;
        }
        return valid;
    }


    public void onValidErrorEmptyEmail() {
        mUsernameEditText.setError(getString(R.string.valid_empty_field));
    }

    public void onValidErrorEmptyPassword() {
        mPasswordEditText.setError(getString(R.string.valid_empty_field));
    }

    public void onValidErrorInvalidEmail() {
        mUsernameEditText.setError(getString(R.string.invalid_email));
    }

    public void showInvalidEmailOrPassword() {
        showSnackbar(R.string.invalid_email_or_password);
    }

    @Override
    protected String getTitle() {
        return null;
    }
}