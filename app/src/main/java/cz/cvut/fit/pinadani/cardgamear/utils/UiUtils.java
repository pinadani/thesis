package cz.cvut.fit.pinadani.cardgamear.utils;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Utilities for UI related things like device size or conversion from dp to px
 * Created on {18/12/15}
 **/
public class UiUtils {
    public static final String TAG = UiUtils.class.getName();

    public static int dpToPx(Context ctx, int dp) {
        Resources resources = ctx.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    /**
     * Obtain host activity from given context.
     *
     * @param context Context
     * @return Activity or null.
     */
    @Nullable
    public static Activity getActivityFromContext(@NonNull Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }

    public static int getStatusBarHeight(Context ctx) {
        int result = 0;
        int resourceId = ctx.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            result = ctx.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void hideIme(View target) {
        if (target != null) {
            InputMethodManager imm = (InputMethodManager) target.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(target.getWindowToken(), 0);
        }
    }

    /**
     * Load bitmap representation of view
     *
     * @param v - view to take screenshot
     * @return bitmap of view
     */
    public static Bitmap loadBitmapFromView(View v) {
        v.setDrawingCacheEnabled(true);
        return v.getDrawingCache();
    }

    public static void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) App.getContext().getSystemService(Context
                .INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void hideToogleKeyboard() {
        InputMethodManager mgr = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);// HIDE_IMPLICIT_ONLY
    }

    public static void showToogleKeyboard() {
        InputMethodManager mgr = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public static void showKeyboard(EditText editText) {
        InputMethodManager mgr = (InputMethodManager) App.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void showKeyboard(Window window) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    public static void hideKeyboard(Window window) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
