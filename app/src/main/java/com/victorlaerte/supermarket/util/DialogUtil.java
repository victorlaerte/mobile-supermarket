package com.victorlaerte.supermarket.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by victoroliveira on 15/01/17.
 */

public class DialogUtil {

	public static void showConfirmationDialog(Context context, String title, String message,
			android.content.DialogInterface.OnClickListener positiveEvent,
			android.content.DialogInterface.OnClickListener negativeEvent) {

		if (negativeEvent == null) {

			negativeEvent = new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// do nothing
				}
			};
		}

		new AlertDialog.Builder(context).setTitle(title)
										.setMessage(message)
										.setPositiveButton(android.R.string.yes, positiveEvent)
										.setNegativeButton(android.R.string.no, negativeEvent)
										.setIcon(android.R.drawable.ic_dialog_alert)
										.show();

	}

	public static void showAlertDialog(Context context, String title, String message) {

		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		alertDialog.setTitle(title);
		alertDialog.setMessage(message);
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(android.R.string.ok),
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
					}
				});
		alertDialog.show();
	}
}
