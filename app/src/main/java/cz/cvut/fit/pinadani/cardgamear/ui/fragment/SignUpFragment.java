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
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.SignUpPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.ISignUpView;
import cz.cvut.fit.pinadani.cardgamear.ui.activity.BaseFragmentActivity;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.base.BaseNucleusFragment;
import cz.cvut.fit.pinadani.cardgamear.utils.UiUtils;
import nucleus.factory.RequiresPresenter;


@RequiresPresenter(SignUpPresenter.class)
public class SignUpFragment extends BaseNucleusFragment<SignUpPresenter> implements ISignUpView {
    public static final String TAG = SignUpFragment.class.getName();

    @Bind(R.id.edit_email)
    EditText mUsernameEditText;
    @Bind(R.id.edit_password)
    EditText mPasswordEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @OnClick(R.id.btn_registration)
    public void onRegistrationClicked() {
        UiUtils.hideKeyboard(mUsernameEditText);
        getPresenter().signUp(mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
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
        return getString(R.string.sign_up_title);
    }

    @Override
    public void signedUp() {
        BaseFragmentActivity.startActivity(getActivity(), MainMenuFragment.class.getName());
        getActivity().finish();
    }

    @Override
    public void pressBack() {

    }

    @Override
    protected void initAB() {
        baseSettingsAB();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void showValidFailEmptyPassword() {
        showSnackbar(R.string.valid_password_fail_empty);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void showValidFailEmpty() {
        showSnackbar(R.string.valid_email_fail_empty);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void showValidFailEmail() {
        showSnackbar(R.string.valid_email_fail_email);
    }
}