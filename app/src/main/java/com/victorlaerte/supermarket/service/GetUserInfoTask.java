package com.victorlaerte.supermarket.service;

/**
 * Created by victoroliveira on 12/01/17.
 */

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.model.Token;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.model.impl.UserImpl;
import com.victorlaerte.supermarket.util.AndroidUtil;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.HttpMethod;
import com.victorlaerte.supermarket.util.HttpUtil;
import com.victorlaerte.supermarket.util.StringPool;
import com.victorlaerte.supermarket.util.SuperMarketUtil;
import com.victorlaerte.supermarket.util.Validator;
import com.victorlaerte.supermarket.view.LoginActivity;

import android.os.AsyncTask;
import android.util.Log;

public class GetUserInfoTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = GetUserInfoTask.class.getName();
	private WeakReference<LoginActivity> wLoginActivity;
	private String url = Constants.LOGIN_BASE_URL + Constants.USER_ENDPOINT;
	private Token token;
	private User user;
	private String errorMsg = StringPool.BLANK;

	public GetUserInfoTask(LoginActivity loginActivity, Token token) {

		wLoginActivity = new WeakReference<LoginActivity>(loginActivity);
		this.token = token;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		Map<String, String> httpParams = new HashMap<String, String>();

		try {

			JSONObject jsonResponse = HttpUtil.sendRequest(url, HttpMethod.GET, httpParams,
					SuperMarketUtil.getAuthString(token.getAccessToken()));

			Log.d(TAG, jsonResponse.toString());

			if (HttpUtil.isHttpSuccess(jsonResponse.getInt(Constants.STATUS_CODE))) {

				JSONObject body = jsonResponse.getJSONObject(Constants.BODY);

				String id = body.getString(Constants.ID);
				String name = body.getString(Constants.NAME);
				String email = body.getString(Constants.EMAIL);

				user = new UserImpl(id, name, email, email, token);

				return true;

			} else {

				errorMsg = jsonResponse.getString(Constants.STATUS_MSG);
			}

		} catch (Exception e) {

			if (Validator.isNotNull(wLoginActivity.get())) {

				errorMsg = AndroidUtil.getString(wLoginActivity.get(), R.string.error_unknown_error);
			}
			Log.e(TAG, e.getMessage());
		}

		return false;
	}

	@Override
	protected void onPostExecute(final Boolean success) {

		LoginActivity loginActivity = wLoginActivity.get();

		if (Validator.isNotNull(loginActivity) && success) {

			loginActivity.onGetUserInfoComplete(success, user, errorMsg);
		}
	}

	@Override
	protected void onCancelled() {

		LoginActivity loginActivity = wLoginActivity.get();

		if (Validator.isNotNull(loginActivity)) {

			loginActivity.onGetUserInfoCanceled();
		}
	}

}