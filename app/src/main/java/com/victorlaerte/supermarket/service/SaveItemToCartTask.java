package com.victorlaerte.supermarket.service;

/**
 * Created by victoroliveira on 12/01/17.
 */

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.model.Cart;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.util.AndroidUtil;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.DialogUtil;
import com.victorlaerte.supermarket.util.HttpMethod;
import com.victorlaerte.supermarket.util.StringPool;
import com.victorlaerte.supermarket.util.SuperMarketUtil;
import com.victorlaerte.supermarket.util.Validator;
import com.victorlaerte.supermarket.util.WebServiceUtil;
import com.victorlaerte.supermarket.view.ItemDetailActivity;

import android.os.AsyncTask;
import android.util.Log;

public class SaveItemToCartTask extends AsyncTask<String, String, Boolean> {

	private static final String TAG = SaveItemToCartTask.class.getName();
	private WeakReference<ItemDetailActivity> wItemDetailActivity;
	private String url = Constants.DATA_BASE_URL + Constants.CART_ENDPOINT;
	private User user;
	private MarketItem marketItem;
	private String cartItemId;
	private String errorMsg = StringPool.BLANK;

	public SaveItemToCartTask(ItemDetailActivity itemDetailActivity, User user, MarketItem marketItem) {

		wItemDetailActivity = new WeakReference<ItemDetailActivity>(itemDetailActivity);
		this.user = user;
		this.marketItem = marketItem;
	}

	protected Boolean doInBackground(String... params) {

		Map<String, String> httpParams = new HashMap<String, String>();
		httpParams.put("productTitle", marketItem.getTitle());
		httpParams.put("productPrice", String.valueOf(marketItem.getPrice()));
		httpParams.put("productFilename", marketItem.getImageFileName());
		httpParams.put("productId", marketItem.getId());
		httpParams.put("userId", user.getId());

		try {

			JSONObject jsonResponse = WebServiceUtil.readJSONResponse(url, HttpMethod.POST, httpParams, true,
					SuperMarketUtil.getAuthString(user.getToken().getAccessToken()));

			Log.d(TAG, jsonResponse.toString());

			if (WebServiceUtil.isHttpSuccess(jsonResponse.getInt(Constants.STATUS_CODE))) {

				cartItemId = jsonResponse.getJSONObject(Constants.BODY).getString(Constants.ID);

				return true;

			} else {

				errorMsg = jsonResponse.getString(Constants.STATUS_MSG);
			}

		} catch (Exception e) {

			errorMsg = AndroidUtil.getString(wItemDetailActivity.get(), R.string.error_unknown_error);
			Log.e(TAG, e.getMessage());
		}

		return false;
	}

	@Override
	protected void onPostExecute(final Boolean success) {

		ItemDetailActivity itemDetailActivity = wItemDetailActivity.get();

		if (Validator.isNotNull(itemDetailActivity) && success) {

			Cart.getInstance().addItem(cartItemId, marketItem);

		} else if (Validator.isNotNull(itemDetailActivity) && !success) {

			DialogUtil.showAlertDialog(itemDetailActivity, itemDetailActivity.getString(R.string.error),
					errorMsg + StringPool.SPACE + itemDetailActivity.getString(R.string.adding_item_to_cart));
		}
	}

}