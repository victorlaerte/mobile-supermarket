package com.victorlaerte.supermarket.service;

/**
 * Created by victoroliveira on 12/01/17.
 */

import java.lang.ref.WeakReference;

import org.json.JSONObject;

import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.model.Cart;
import com.victorlaerte.supermarket.model.CartItem;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.model.impl.CartItemImpl;
import com.victorlaerte.supermarket.util.AndroidUtil;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.DialogUtil;
import com.victorlaerte.supermarket.util.HttpMethod;
import com.victorlaerte.supermarket.util.HttpUtil;
import com.victorlaerte.supermarket.util.StringPool;
import com.victorlaerte.supermarket.util.SuperMarketUtil;
import com.victorlaerte.supermarket.util.Validator;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

public class SaveItemToCartTask extends AsyncTask<String, String, Boolean> {

	private static final String TAG = SaveItemToCartTask.class.getName();
	private WeakReference<Activity> wActivity;
	private String url = Constants.DATA_BASE_URL + Constants.CART_ENDPOINT;
	private User user;
	private MarketItem marketItem;
	private CartItem cartItem;
	private String errorMsg = StringPool.BLANK;

	public SaveItemToCartTask(Activity activity, User user, MarketItem marketItem) {

		wActivity = new WeakReference<Activity>(activity);
		this.user = user;
		this.marketItem = marketItem;
	}

	protected Boolean doInBackground(String... params) {

		try {

			JSONObject jsonParams = new JSONObject();
			jsonParams.put("productTitle", marketItem.getTitle());
			jsonParams.put("productPrice", marketItem.getPrice());
			jsonParams.put("productFilename", marketItem.getImageFileName());
			jsonParams.put("productId", marketItem.getId());
			jsonParams.put("userId", user.getId());

			JSONObject jsonResponse = HttpUtil.sendRequest(url, HttpMethod.POST, jsonParams,
					SuperMarketUtil.getAuthString(user.getToken().getAccessToken()));

			Log.d(TAG, jsonResponse.toString());

			if (HttpUtil.isHttpSuccess(jsonResponse.getInt(Constants.STATUS_CODE))) {

				String cartItemId = jsonResponse.getJSONObject(Constants.BODY).getString(Constants.ID);

				cartItem = new CartItemImpl(cartItemId, marketItem);
				return true;

			} else {

				errorMsg = jsonResponse.getString(Constants.STATUS_MSG);
			}

		} catch (Exception e) {

			if (wActivity.get() != null) {
				errorMsg = AndroidUtil.getString(wActivity.get(), R.string.error_unknown_error);
			}
			Log.e(TAG, e.getMessage());
		}

		return false;
	}

	@Override
	protected void onPostExecute(final Boolean success) {

		Activity activity = wActivity.get();

		if (Validator.isNotNull(activity) && success) {

			Cart.getInstance().addItem(cartItem);

		} else if (Validator.isNotNull(activity) && !success) {

			DialogUtil.showAlertDialog(activity, activity.getString(R.string.error),
					errorMsg + StringPool.SPACE + activity.getString(R.string.adding_item_to_cart));
		}
	}

}