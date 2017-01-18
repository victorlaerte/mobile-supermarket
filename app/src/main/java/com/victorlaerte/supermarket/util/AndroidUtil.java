package com.victorlaerte.supermarket.util;

/**
 * Created by Victor on 11/01/2017.
 */

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

public class AndroidUtil {

    public static String getColor(Context context, int colorId) {

        return context.getString(colorId);
    }

    public static String getString(Context context, int stringId) {

        return context.getString(stringId);
    }

    public static String getString(Context context, int... stringId) {

        return getString(context, StringPool.SPACE, stringId);
    }

    public static String getString(Context context, String separator, int... stringId) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < stringId.length; i++) {

            if (i > 0) {
                sb.append(separator);
            }

            sb.append(context.getString(stringId[i]));
        }

        return sb.toString();
    }

    public static boolean isNetworkAvaliable(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }

        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public static boolean isAnimationAvailable() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            return true;
        } else {
            return false;
        }
    }

    public static int convertDpToPixel(float dp, Context context) {

        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;

        // Convert the dps to pixels, based on density scale
        int px = (int) (dp * scale + 0.5f);

        return px;
    }

    public static float convertPixelsToDp(float px, Context context) {

        Resources resources = context.getResources();

        DisplayMetrics metrics = resources.getDisplayMetrics();

        float dp = px / (metrics.densityDpi / 160f);

        return dp;
    }

    public static boolean clearSharedPreferences(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = preferences.edit();

        edit.clear();
        return edit.commit();
    }

    public static void saveToSharedPreferences(Context context, String key, String value) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(key, value).commit();
    }

    public static void saveToSharedPreferences(Context context, Map<String, String> map) {

        for (Map.Entry<String, String> entry : map.entrySet()) {

            saveToSharedPreferences(context, entry.getKey(), entry.getValue());
        }
    }

}