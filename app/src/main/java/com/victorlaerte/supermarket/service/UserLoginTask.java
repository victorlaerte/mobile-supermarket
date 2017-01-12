package com.victorlaerte.supermarket.service;

/**
 * Created by victoroliveira on 12/01/17.
 */

import java.lang.ref.WeakReference;

import com.victorlaerte.supermarket.util.Validator;
import com.victorlaerte.supermarket.view.LoginActivity;

import android.os.AsyncTask;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	private static final String[] DUMMY_CREDENTIALS = new String[] { "foo@example.com:hello", "bar@example.com:world" };

	private final String mEmail;
	private final String mPassword;
	private WeakReference<LoginActivity> wLoginActivity;

	public UserLoginTask(LoginActivity loginActivity, String email, String password) {

		wLoginActivity = new WeakReference<LoginActivity>(loginActivity);
		mEmail = email;
		mPassword = password;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO: attempt authentication against a network service.

		try {
			// Simulate network access.
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			return false;
		}

		for (String credential : DUMMY_CREDENTIALS) {
			String[] pieces = credential.split(":");
			if (pieces[0].equals(mEmail)) {
				// Account exists, return true if the password matches.
				return pieces[1].equals(mPassword);
			}
		}

		// TODO: register the new account here.
		return true;
	}

	@Override
	protected void onPostExecute(final Boolean success) {

		LoginActivity loginActivity = wLoginActivity.get();

		if (Validator.isNotNull(loginActivity)) {

			loginActivity.onLoginComplete(success);
		}
	}

	@Override
	protected void onCancelled() {

		LoginActivity loginActivity = wLoginActivity.get();

		if (Validator.isNotNull(loginActivity)) {

			loginActivity.onLoginCanceled();
		}
	}
}