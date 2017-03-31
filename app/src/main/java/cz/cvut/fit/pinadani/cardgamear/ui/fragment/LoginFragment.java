package cz.cvut.fit.pinadani.cardgamear.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.LoginPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.ILoginView;
import cz.cvut.fit.pinadani.cardgamear.ui.activity.BaseFragmentActivity;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.base.BaseNucleusFragment;
import cz.cvut.fit.pinadani.cardgamear.utils.UiUtils;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(LoginPresenter.class)
public class LoginFragment extends BaseNucleusFragment<LoginPresenter> implements ILoginView {
    public static final String TAG = LoginFragment.class.getName();

    @Bind(R.id.edit_email)
    EditText mUsernameEditText;
    @Bind(R.id.edit_password)
    EditText mPasswordEditText;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_login)
    public void onLoginButtonClicked() {
        UiUtils.hideKeyboard(mUsernameEditText);
        getPresenter().login(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
    }

    @OnClick(R.id.btn_forgot_password)
    public void onForgotPasswordClicked() {
        BaseFragmentActivity.startActivity(getActivity(), ForgotPasswordFragment.class.getName());
    }

    @OnClick(R.id.btn_registration)
    public void onRegistrationClicked() {
        BaseFragmentActivity.startActivity(getActivity(), SignUpFragment.class.getName());
    }

    @Override
    public void showLoginFail() {

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
        return " ";
    }

    @Override
    public void pressBack() {

    }

    @Override
    protected void initAB() {
        baseSettingsAB();
    }
}