package com.victorlaerte.supermarket.util;

/**
 * Created by Victor on 11/01/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
}