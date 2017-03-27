package cz.cvut.fit.pinadani.cardgamear.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.cvut.fit.pinadani.cardgamear.R;

/**
 * Fragment to get forgot password
 * Created by Daniel Pina
 **/
public class ForgotPasswordFragment extends BaseFragment {
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
        return null;
    }

//    @Override
//    protected String getTitle() {
//        return getString(R.string.forgot_password_title);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.done, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_done:
//                GoogleAnalyticsUtils
//                        .trackSettingsEvent(R.string.google_analytics_action_new_password);
//                View view = getActivity().getCurrentFocus();
//                if (view != null) {
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                }
//                getPresenter().sendEmail(mEditEmail.getText().toString());
//                return true;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected String getGAName() {
//        return TAG;
//    }
//
//    @Override
//    public void showProgress(boolean showProgress) {
//        mProgress.setVisibility(showProgress ? View.VISIBLE : View.GONE);
//    }
//
//    @Override
//    public void showStatus(boolean status) {
//        if (status) {
//            Snackbar.make(getView(), R.string.email_was_send, Snackbar.LENGTH_LONG).show();
//        } else {
//            Snackbar.make(getView(), R.string.send_email_failed, Snackbar.LENGTH_LONG).show();
//        }
//
//    }
//
//    @Override
//    public void pressBack() {
//        GoogleAnalyticsUtils
//                .trackSettingsEvent(R.string.google_analytics_action_new_password_back);
//    }
//
//    @SuppressWarnings("ConstantConditions")
//    @Override
//    public void showValidFailEmpty() {
//        Snackbar.make(getView(), R.string.valid_email_fail_empty, Snackbar.LENGTH_LONG).show();
//    }
//
//    @SuppressWarnings("ConstantConditions")
//    @Override
//    public void showValidFailEmail() {
//        Snackbar.make(getView(), R.string.valid_email_fail_email, Snackbar.LENGTH_LONG).show();
//    }
}
