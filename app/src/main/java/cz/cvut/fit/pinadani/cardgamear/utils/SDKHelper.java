package cz.cvut.fit.pinadani.cardgamear.utils;

public class SDKHelper {
    public static final String TAG = SDKHelper.class.getName();

    public static String removeWhiteSpacesFromEnd(String input) {
        if (input != null) {
            return input.replaceAll("\\s+$", "");
        } else {
            return null;
        }
    }

    public static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
