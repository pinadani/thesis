package cz.cvut.fit.pinadani.cardgamear.ui.fragment;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import cz.cvut.fit.pinadani.cardgamear.R;
import cz.cvut.fit.pinadani.cardgamear.ui.fragment.dialog.ProgressDialogFragment;
import cz.cvut.fit.pinadani.cardgamear.utils.Constants;

public abstract class BaseFragment extends Fragment {
    public static final String TAG = BaseFragment.class.getName();


    private final String PROGRESS_FRAGMENT = "progressFragment";

    //private FragmentDelegate delegate;

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

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            setTitle(getTitle());
        }
    }

    protected void setTitle(String title) {
        //delegate.setTitle(title);
    }

    protected abstract String getTitle();

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
        Snackbar.make(getView(), msg, Snackbar.LENGTH_LONG).show();
    }

    protected void showSnackbar(int msgId) {
        Snackbar.make(getView(), msgId, Snackbar.LENGTH_LONG).show();
    }
}
