package com.victorlaerte.supermarket.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.victorlaerte.supermarket.LoginException;
import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.SignUpException;
import com.victorlaerte.supermarket.model.Token;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.model.impl.TokenImpl;
import com.victorlaerte.supermarket.model.impl.UserImpl;
import com.victorlaerte.supermarket.service.GetUserInfoTask;
import com.victorlaerte.supermarket.service.UserLoginTask;
import com.victorlaerte.supermarket.service.UserSignUpTask;
import com.victorlaerte.supermarket.util.AndroidUtil;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.DialogUtil;
import com.victorlaerte.supermarket.util.StringPool;
import com.victorlaerte.supermarket.util.Validator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

	private static final String TAG = LoginActivity.class.getName();
	private LinearLayout nameLayout;
	private EditText nameView;
	private EditText emailView;
	private EditText passwordView;
	private Button primaryActionButton;
	private Button secondaryActionButton;
	private View progressView;
	private View loginFormView;
	private View signupFormView;
	private boolean signUp = false;
	private UserSignUpTask signUpTask = null;
	private UserLoginTask userLoginTask = null;
	private GetUserInfoTask getUserInfoTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		emailView = (EditText) findViewById(R.id.email);
		nameView = (EditText) findViewById(R.id.name);
		passwordView = (EditText) findViewById(R.id.password);
		loginFormView = findViewById(R.id.login_form);
		progressView = findViewById(R.id.login_progress);
		primaryActionButton = (Button) findViewById(R.id.primary_action_button);
		secondaryActionButton = (Button) findViewById(R.id.secondary_action_button);

		passwordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {

				if (id == R.id.login || id == EditorInfo.IME_NULL) {
					attemptLoginOrSignUp();
					return true;
				}
				return false;
			}
		});

		primaryActionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				attemptLoginOrSignUp();
			}
		});

		secondaryActionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				signUp = !signUp;

				showSignUpFormToggle(signUp);
			}
		});

		tryPersistentLogin();
	}

	private void tryPersistentLogin() {

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String userStr = preferences.getString(Constants.USER, null);

		if (Validator.isNotNull(userStr)) {

			try {

				JSONObject userJSON = new JSONObject(userStr);

				User user = new UserImpl(userJSON);

				if (Validator.isNotNull(user)) {

					initItemListActivity(user);
				}

			} catch (JSONException e) {
				Log.e(TAG, e.getMessage());
			}
		}
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	private void attemptLoginOrSignUp() {

		boolean cancel = false;
		View focusView = null;

		// Reset errors.
		nameView.setError(null);
		emailView.setError(null);
		passwordView.setError(null);

		String name = nameView.getText().toString();
		String email = emailView.getText().toString();
		String password = passwordView.getText().toString();

		if (TextUtils.isEmpty(password)) {
			passwordView.setError(getString(R.string.error_field_required));
			focusView = passwordView;
			cancel = true;
		}

		if (TextUtils.isEmpty(email)) {
			emailView.setError(getString(R.string.error_field_required));
			focusView = emailView;
			cancel = true;
		} else if (!Validator.isEmailValid(email)) {
			emailView.setError(getString(R.string.error_invalid_email));
			focusView = emailView;
			cancel = true;
		}

		if (signUp) {

			if (TextUtils.isEmpty(name)) {
				nameView.setError(getString(R.string.error_field_required));
				focusView = nameView;
				cancel = true;
			}

			if (!isPasswordValid(password)) {

				passwordView.setError(getString(R.string.error_incorrect_password));
				focusView = passwordView;
				cancel = true;
			}
		}

		if (cancel) {

			focusView.requestFocus();

		} else {

			AndroidUtil.hideSoftKeyboard(this);

			if (AndroidUtil.isNetworkAvaliable(this)) {

				showProgress(true);

				if (signUp && Validator.isNull(signUpTask) && Validator.isNull(userLoginTask)) {

					signUpTask = new UserSignUpTask(this, name, email, password);
					signUpTask.execute();

				} else if (Validator.isNull(userLoginTask) && Validator.isNull(signUpTask)) {

					userLoginTask = new UserLoginTask(this, email, password);
					userLoginTask.execute();

				}

			} else {

				DialogUtil.showAlertDialog(this, getString(R.string.error),
						getString(R.string.erro_no_internet_connection));
			}
		}
	}

	private boolean isPasswordValid(String password) {

		// TODO: This method should be implement with the same password policy from http://public.mobilesupermarket.wedeploy.io/
		return password.length() >= 6 && !password.contains(StringPool.SPACE);
	}

	private void showSignUpFormToggle(boolean show) {

		nameView.setText(StringPool.BLANK);

		nameView.setVisibility(signUp ? View.VISIBLE : View.GONE);

		if (signUp) {
			nameView.requestFocus();
		}

		primaryActionButton.setText(signUp ? getString(R.string.action_register) : getString(R.string.action_login));

		secondaryActionButton.setText(signUp ? getString(R.string.action_back) : getString(R.string.action_sign_up));

	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {

		int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

		if (AndroidUtil.isAnimationAvailable()) {

			loginFormView	.animate()
							.setDuration(shortAnimTime)
							.alpha(show ? 0 : 1)
							.setListener(new AnimatorListenerAdapter() {

								@Override
								public void onAnimationEnd(Animator animation) {

									loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
								}
							});

			progressView.animate()
						.setDuration(shortAnimTime)
						.alpha(show ? 1 : 0)
						.setListener(new AnimatorListenerAdapter() {

							@Override
							public void onAnimationEnd(Animator animation) {

								progressView.setVisibility(show ? View.VISIBLE : View.GONE);
							}
						});
		} else {

			progressView.setVisibility(show ? View.VISIBLE : View.GONE);
			loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}

	}

	public void onSignUpComplete(boolean success, JSONObject jsonResponse) {

		showProgress(false);

		try {

			if (success) {

				successfulSignUp(jsonResponse);

			} else {

				unsuccessfulSignUp(jsonResponse);
			}

		} catch (SignUpException e) {

			DialogUtil.showAlertDialog(this, getString(R.string.error), e.getMessage());

		} finally {

			signUpTask = null;
		}

	}

	private void successfulSignUp(JSONObject jsonResponse) throws SignUpException {

		String name = StringPool.BLANK;
		String email = StringPool.BLANK;
		String strCreatedDate = StringPool.BLANK;
		Date createdDate = null;

		try {

			name = jsonResponse.getJSONObject(Constants.BODY).getString(Constants.NAME);
			email = jsonResponse.getJSONObject(Constants.BODY).getString(Constants.EMAIL);
			strCreatedDate = jsonResponse.getJSONObject(Constants.BODY).getString(Constants.CREATED_AT);
			createdDate = new Date(Long.valueOf(strCreatedDate));

			if (!email.isEmpty()) {

				DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
				String msg = String.format(getString(R.string.successful_sign_up), name, df.format(createdDate));
				DialogUtil.showAlertDialog(this, getString(R.string.info), msg);

				emailView.requestFocus();
				signUp = false;
				showSignUpFormToggle(signUp);

			} else {

				throw new SignUpException(getString(R.string.error_unknown_error) + "\n" + getString(
						R.string.error_contact_administrator));
			}

		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			throw new SignUpException(e.getMessage(), e);
		}
	}

	private void unsuccessfulSignUp(JSONObject jsonResponse) throws SignUpException {

		int statusCode = 0;
		String statusMsg = StringPool.BLANK;

		try {

			statusCode = jsonResponse.getInt(Constants.STATUS_CODE);

			if (statusCode != 0) {

				JSONArray jsonArrayErrors = jsonResponse.getJSONObject(Constants.BODY).getJSONArray(Constants.ERRORS);

				StringBuilder sb = new StringBuilder(StringPool.NEW_LINE);
				sb.append(getString(R.string.error_sign_up));

				for (int i = 0; i < jsonArrayErrors.length(); i++) {

					JSONObject curJSON = jsonArrayErrors.getJSONObject(i);

					String reason = StringPool.BLANK;
					String msg = StringPool.BLANK;

					if (curJSON.has(Constants.REASON)) {
						reason = curJSON.getString(Constants.REASON);
					}

					if (curJSON.has(Constants.MESSAGE)) {
						msg = curJSON.getString(Constants.MESSAGE);
					}

					/* This message depends on server response, maybe some documentation will be useful here */
					sb.append(getString(R.string.reason_colon));
					sb.append(StringPool.NEW_LINE);
					sb.append(reason);
					sb.append(StringPool.SPACE);
					sb.append(msg);
				}

				statusMsg = sb.toString();

			} else {

				statusMsg = jsonResponse.getString(Constants.STATUS_MSG);
				statusMsg += "\n" + getString(R.string.error_contact_administrator);
			}

			throw new SignUpException(statusMsg);

		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			throw new SignUpException(e.getMessage(), e);
		}

	}

	public void onLoginComplete(boolean success, JSONObject jsonResponse) {

		showProgress(false);

		try {

			if (success) {

				successfulLogin(jsonResponse);

			} else {

				unsuccessfulLogin(jsonResponse);
			}

		} catch (LoginException e) {

			DialogUtil.showAlertDialog(this, getString(R.string.error), e.getMessage());

		} finally {

			userLoginTask = null;
		}
	}

	private void successfulLogin(JSONObject jsonResponse) throws LoginException {

		String email = emailView.getText().toString();

		String accessToken = StringPool.BLANK;
		String tokenType = StringPool.BLANK;

		try {

			accessToken = jsonResponse.getJSONObject(Constants.BODY).getString(Constants.ACCESS_TOKEN);
			tokenType = jsonResponse.getJSONObject(Constants.BODY).getString(Constants.TOKEN_TYPE);

			if (!accessToken.isEmpty()) {

				Token token = new TokenImpl(accessToken, tokenType);

				getUserInfoTask = new GetUserInfoTask(LoginActivity.this, token);
				getUserInfoTask.execute();

			} else {

				throw new LoginException(getString(R.string.error_unknown_error) + "\n" + getString(
						R.string.error_contact_administrator));
			}

		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			throw new LoginException(e.getMessage(), e);
		}
	}

	private void initItemListActivity(User user) {

		Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
		intent.putExtra(Constants.USER, user);

		startActivity(intent);

		finish();
	}

	private void unsuccessfulLogin(JSONObject jsonResponse) throws LoginException {

		int statusCode = 0;
		String statusMsg = StringPool.BLANK;

		try {

			statusCode = jsonResponse.getInt(Constants.STATUS_CODE);

			if (statusCode != 0) {

				statusMsg = jsonResponse.getJSONObject(Constants.BODY).getString(Constants.ERROR_DESCRIPTION);

				/*
				 * FIX:
				 * Server response for Invalid user credentials should be 404 instead 400
				 * {"error_description":"Invalid user credentials","error":"invalid_request","statusCode":400,"statusMsg":"Bad request"}
				 */
				emailView.setError(getString(R.string.error_invalid_credentials));
				passwordView.setError(getString(R.string.error_invalid_credentials));

			} else {

				statusMsg = jsonResponse.getString(Constants.STATUS_MSG);
				statusMsg += "\n" + getString(R.string.error_contact_administrator);
				throw new LoginException(statusMsg);
			}

		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			throw new LoginException(e.getMessage(), e);
		}

	}

	public void onGetUserInfoComplete(boolean success, User user, String errorMsg) {

		showProgress(false);

		try {

			if (success) {

				successfulGetUserInfo(user);

			} else {

				DialogUtil.showAlertDialog(this, getString(R.string.error), errorMsg);
			}

		} finally {

			getUserInfoTask = null;
		}
	}

	private void successfulGetUserInfo(User user) {

		AndroidUtil.saveToSharedPreferences(getApplicationContext(), Constants.USER, user.toString());

		initItemListActivity(user);
	}

	public void onSignUpCanceled() {

		signUpTask = null;
		showProgress(false);
	}

	public void onLoginCanceled() {

		userLoginTask = null;
		showProgress(false);
	}

	public void onGetUserInfoCanceled() {

		getUserInfoTask = null;
		showProgress(false);
	}
}