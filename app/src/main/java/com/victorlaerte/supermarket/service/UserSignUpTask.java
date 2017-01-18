package com.victorlaerte.supermarket.service;

/**
 * Created by victoroliveira on 12/01/17.
 */

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.HttpMethod;
import com.victorlaerte.supermarket.util.HttpUtil;
import com.victorlaerte.supermarket.util.JSONUtil;
import com.victorlaerte.supermarket.util.Validator;
import com.victorlaerte.supermarket.view.LoginActivity;

import android.os.AsyncTask;

public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = UserSignUpTask.class.getName();
	private final String name;
	private final String email;
	private final String password;
	private WeakReference<LoginActivity> wLoginActivity;
	private String url = Constants.LOGIN_BASE_URL + Constants.SIGN_UP_ENDPOINT;
	private JSONObject jsonResponse;

	public UserSignUpTask(LoginActivity loginActivity, String name, String email, String password) {

		wLoginActivity = new WeakReference<LoginActivity>(loginActivity);
		this.name = name;
		this.email = email;
		this.password = password;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		Map<String, String> httpParams = new HashMap<String, String>();

		httpParams.put(Constants.NAME, name);
		httpParams.put(Constants.EMAIL, email);
		httpParams.put(Constants.PASSWORD, password);

		try {

			jsonResponse = HttpUtil.sendRequest(url, HttpMethod.POST, httpParams, null);

			if (HttpUtil.isHttpSuccess(jsonResponse.getInt(Constants.STATUS_CODE))) {

				return true;
			}

		} catch (Exception e) {

			jsonResponse = JSONUtil.createJSONErrorResponseHTTPLike(e);
		}

		return false;
	}

	@Override
	protected void onPostExecute(final Boolean success) {

		LoginActivity loginActivity = wLoginActivity.get();

		if (Validator.isNotNull(loginActivity)) {

			loginActivity.onSignUpComplete(success, jsonResponse);
		}
	}

	@Override
	protected void onCancelled() {

		LoginActivity loginActivity = wLoginActivity.get();

		if (Validator.isNotNull(loginActivity)) {

			loginActivity.onSignUpCanceled();
		}
	}
}