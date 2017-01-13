package com.victorlaerte.supermarket.service;

/**
 * Created by victoroliveira on 12/01/17.
 */

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONException;
import org.json.JSONObject;

import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.HttpMethod;
import com.victorlaerte.supermarket.util.StringPool;
import com.victorlaerte.supermarket.util.Validator;
import com.victorlaerte.supermarket.util.WebServiceUtil;
import com.victorlaerte.supermarket.view.LoginActivity;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Represents an asynchronous login task used to authenticate the user.
 */
public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = UserLoginTask.class.getName();
	private final String email;
	private final String password;
	private WeakReference<LoginActivity> wLoginActivity;
	private String url = Constants.LOGIN_BASE_URL + Constants.LOGIN_AUTH_ENDPOINT;

	public UserLoginTask(LoginActivity loginActivity, String email, String password) {

		wLoginActivity = new WeakReference<LoginActivity>(loginActivity);
		this.email = email;
		this.password = password;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		Map<String, String> httpParams = new HashMap<String, String>();

		httpParams.put(StringPool.USERNAME, email);
		httpParams.put(StringPool.PASSWORD, password);
		httpParams.put("grant_type", StringPool.PASSWORD);

		try {

			JSONObject response = WebServiceUtil.readJSONResponse(url, HttpMethod.POST, httpParams);

			Log.d(TAG, response.toString());

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

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