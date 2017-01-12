package com.victorlaerte.supermarket.view;

import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.service.UserLoginTask;
import com.victorlaerte.supermarket.util.AndroidUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

	private EditText nameView;
	private EditText emailView;
	private EditText passwordView;
	private View progressView;
	private View loginFormView;
	private boolean signUp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		emailView = (EditText) findViewById(R.id.email);
		nameView = (EditText) findViewById(R.id.name);
		passwordView = (EditText) findViewById(R.id.password);
		loginFormView = findViewById(R.id.login_form);
		progressView = findViewById(R.id.login_progress);

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

		Button emailLoginButton = (Button) findViewById(R.id.email_login_button);
		emailLoginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				attemptLoginOrSignUp();
			}
		});

		Button signUpButton = (Button) findViewById(R.id.sign_up_button);
		signUpButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				showSignUpForm(!signUp);
			}
		});
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	private void attemptLoginOrSignUp() {

		// Reset errors.
		emailView.setError(null);
		passwordView.setError(null);

		// Store values at the time of the login attempt.
		String email = emailView.getText().toString();
		String password = passwordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password, if the user entered one.
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			passwordView.setError(getString(R.string.error_invalid_password));
			focusView = passwordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			emailView.setError(getString(R.string.error_field_required));
			focusView = emailView;
			cancel = true;
		} else if (!isEmailValid(email)) {
			emailView.setError(getString(R.string.error_invalid_email));
			focusView = emailView;
			cancel = true;
		}

		if (cancel) {

			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();

		} else {

			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.

			AndroidUtil.hideSoftKeyboard(this);

			showProgress(true);
			UserLoginTask mAuthTask = new UserLoginTask(this, email, password);
			mAuthTask.execute((Void) null);
		}
	}

	private boolean isEmailValid(String email) {

		//TODO: Replace this with your own logic
		return email.contains("@");
	}

	private boolean isPasswordValid(String password) {

		//TODO: Replace this with your own logic
		return password.length() > 4;
	}

	private boolean isAnimationAvailable() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
			// for very easy animations. If available, use these APIs to animate
			return true;
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			return false;
		}
	}

	private void showSignUpForm(boolean show) {

		int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
		signUp = show;

		if (isAnimationAvailable()) {

			nameView.animate()
					.setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {

						@Override
						public void onAnimationEnd(Animator animation) {

							nameView.setVisibility(signUp ? View.VISIBLE : View.GONE);
						}
					});
		} else {

			nameView.setVisibility(signUp ? View.VISIBLE : View.GONE);

		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {

		int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

		if (isAnimationAvailable()) {

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

	public void onLoginComplete(boolean success) {

		showProgress(false);

		if (success) {

			Intent intent = new Intent(getApplicationContext(), ItemListActivity.class);
			startActivity(intent);

			finish();
		} else {
			passwordView.setError(getString(R.string.error_incorrect_password));
			passwordView.requestFocus();
		}
	}

	public void onLoginCanceled() {

		showProgress(false);
	}
}