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
import android.util.Log;

public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = UserLoginTask.class.getName();
	private final String email;
	private final String password;
	private WeakReference<LoginActivity> wLoginActivity;
	private String url = Constants.LOGIN_BASE_URL + Constants.LOGIN_AUTH_ENDPOINT;
	private JSONObject jsonResponse;

	public UserLoginTask(LoginActivity loginActivity, String email, String password) {

		wLoginActivity = new WeakReference<LoginActivity>(loginActivity);
		this.email = email;
		this.password = password;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		Map<String, String> httpParams = new HashMap<String, String>();

		httpParams.put(Constants.USERNAME, email);
		httpParams.put(Constants.PASSWORD, password);
		httpParams.put(Constants.GRANT_TYPE, Constants.PASSWORD);

		try {

			jsonResponse = HttpUtil.sendRequest(url, HttpMethod.POST, httpParams, null);

			Log.d(TAG, jsonResponse.toString());

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

			loginActivity.onLoginComplete(success, jsonResponse);
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