package cz.cvut.fit.pinadani.cardgamear.ui.fragment.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import cz.cvut.fit.pinadani.cardgamear.R;


public class ProgressDialogFragment extends DialogFragment {

    public interface ProgressDialogListener {
        void onProgressButtonClicked();
    }

    private static final String IS_CANCELABLE = "isCancelable";

    public static ProgressDialogFragment createInstance(boolean isCancelable) {
        ProgressDialogFragment fragment = new ProgressDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putBoolean(IS_CANCELABLE, isCancelable);
        fragment.setArguments(arguments);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            setCancelable(args.getBoolean(IS_CANCELABLE, false));
        }
        return setupDialogButton(getActivity(), getProgressDialog(getActivity()));
    }

    private Dialog setupDialogButton(final FragmentActivity activity, ProgressDialog dialog) {

        if (getArguments() != null && getArguments().getBoolean(IS_CANCELABLE, false)) {
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (activity instanceof ProgressDialogListener) {
                        ((ProgressDialogListener) activity).onProgressButtonClicked();
                    }
                }
            });
        }
        return dialog;
    }

    public static ProgressDialog getProgressDialog(final FragmentActivity activity) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage(activity.getString(R.string.text_wait));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        return dialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (activity instanceof ProgressDialogListener) {
                ((ProgressDialogListener) activity).onProgressButtonClicked();
            }
        }
    }

}
