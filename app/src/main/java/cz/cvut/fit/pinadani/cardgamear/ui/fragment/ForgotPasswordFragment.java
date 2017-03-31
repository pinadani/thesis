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
import cz.cvut.fit.pinadani.cardgamear.mvp.presenter.ForgotPasswordPresenter;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IForgotPasswordView;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.base.BaseNucleusFragment;
import cz.cvut.fit.pinadani.cardgamear.utils.UiUtils;
import nucleus.factory.RequiresPresenter;

/**
 * Fragment to get forgot password
 * Created by Daniel Pina
 **/
@RequiresPresenter(ForgotPasswordPresenter.class)
public class ForgotPasswordFragment extends BaseNucleusFragment<ForgotPasswordPresenter>
        implements IForgotPasswordView {
    public static final String TAG = ForgotPasswordFragment.class.getName();

    @Bind(R.id.edit_email)
    EditText mEditEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.forgot_password_title);
    }

    @Override
    public void pressBack() {
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

    @OnClick(R.id.btn_reset_password)
    public void onResetPasswordClicked(){
        UiUtils.hideKeyboard(mEditEmail);
        getPresenter().sendEmail(mEditEmail.getText().toString());
    }

    @Override
    protected void initAB() {
        baseSettingsAB();
    }
}
