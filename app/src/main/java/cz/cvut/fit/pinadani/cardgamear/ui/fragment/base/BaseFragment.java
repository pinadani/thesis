package cz.cvut.fit.pinadani.cardgamear.ui.fragment.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.view.View;

import butterknife.ButterKnife;
import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.mvp.view.IBaseView;
import cz.cvut.fit.pinadani.cardgamear.ui.activity.BaseFragmentActivity;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.dialog.ProgressDialogFragment;
import cz.cvut.fit.pinadani.cardgamear.utils.Constants;
import cz.cvut.fit.pinadani.cardgamear.utils.UiUtils;

public abstract class BaseFragment extends Fragment implements IBaseFragment, IBaseView {
    public static final String TAG = BaseFragment.class.getName();


    private final String PROGRESS_FRAGMENT = "progressFragment";

    private FragmentDelegate delegate;

    public BaseFragment() {
        if (getArguments() == null) {
            setArguments(new Bundle());
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        delegate = new FragmentDelegate();
        delegate.onCreate(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAB();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            setTitle(getTitle());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideIme();
    }

    @Override
    public void onDestroy() {
        delegate.onDestroy();
        delegate = null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onUpButtonClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when back button is pressed
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void pressBack() {
    }

    /**
     * Called when up is clicked
     */
    public void onUpButtonClicked() {
    }

    protected void setTitle(int title) {
        delegate.setTitle(title);
    }

    public BaseFragmentActivity getFragmentActivity() {
        return (BaseFragmentActivity) getActivity();
    }

    protected abstract String getTitle();

    protected void setTitle(String title) {
        delegate.setTitle(title);
    }

    protected abstract void initAB();

    protected void baseSettingsAB() {
        delegate.baseSettingsAB();
    }

    /**
     * Hides keyboard if it is shown
     */
    protected void hideIme() {
        if (getView() != null) { // try to hide keyboard when destroying fragment view
            View focusedView = getView().findFocus();
            if (focusedView != null) {
                UiUtils.hideIme(focusedView);
            }
        }
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void showProgress(boolean show) {
        if (show) {
            showProgressDialog(getFragmentManager(), false);
        } else {
            dismissProgressDialog();
        }
    }

    protected void showProgressDialog(FragmentManager fm, boolean isCancelable) {
        try {
            DialogFragment dialogFragment = getProgressDialog(fm);

            if (dialogFragment != null) {
                // v ramci dismiss nemusi byt odebran - napr. AudioDetail
                if (dialogFragment.getDialog() != null) {
                    if (!dialogFragment.getDialog().isShowing()) {
                        fm.beginTransaction().remove(dialogFragment).commit();
                    } else {
                        // neni potreba vytvaret novy - prave je zobrazen
                        return;
                    }
                } else {
                    fm.beginTransaction().remove(dialogFragment).commit();
                }

            }

            dialogFragment = ProgressDialogFragment.createInstance(isCancelable);
            dialogFragment.show(fm, PROGRESS_FRAGMENT);

            fm.executePendingTransactions();
        } catch (Exception e) {
            if (Constants.DEBUG) {
                e.printStackTrace();
            }
            // TODO: vyresit lepe - kvuli inside of onLoadFinished
        }
    }


    protected void dismissProgressDialog() {
        FragmentManager fm = getFragmentManager();
        DialogFragment fragment = getProgressDialog(fm);

        if (fragment != null) {
            fragment.dismissAllowingStateLoss();
            fm.executePendingTransactions();
        }
    }

    protected DialogFragment getProgressDialog(FragmentManager fm) {
        return (DialogFragment) (fm.findFragmentByTag(PROGRESS_FRAGMENT));
    }

    protected void showSnackbar(String msg) {
        if (getView() != null) {
            Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
        }
    }

    public void showSnackbar(int msgId) {
        if (getView() != null) {
            Snackbar.make(getView(), msgId, Snackbar.LENGTH_LONG).show();
        }
    }
}
