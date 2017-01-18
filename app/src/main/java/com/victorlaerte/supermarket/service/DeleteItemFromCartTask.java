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
import com.victorlaerte.supermarket.view.ItemListActivity;

import android.os.AsyncTask;
import android.util.Log;

public class DeleteItemFromCartTask extends AsyncTask<String, String, Boolean> {

	private static final String TAG = DeleteItemFromCartTask.class.getName();
	private WeakReference<ItemListActivity> wItemListActivity;
	private String url = Constants.DATA_BASE_URL + Constants.CART_ENDPOINT;
	private User user;
	private String cartItemId;
	private MarketItem marketItem;
	private String errorMsg = StringPool.BLANK;

	public DeleteItemFromCartTask(ItemListActivity itemListActivity, User user, String cartItemId,
			MarketItem marketItem) {

		wItemListActivity = new WeakReference<ItemListActivity>(itemListActivity);
		this.user = user;
		this.cartItemId = cartItemId;
		this.marketItem = marketItem;
	}

	protected Boolean doInBackground(String... params) {

		Map<String, String> httpParams = new HashMap<String, String>();

		url += StringPool.FORWARD_SLASH + cartItemId;

		try {

			JSONObject jsonResponse = WebServiceUtil.readJSONResponse(url, HttpMethod.DELETE, httpParams, true,
					SuperMarketUtil.getAuthString(user.getToken().getAccessToken()));

			Log.d(TAG, jsonResponse.toString());

			if (WebServiceUtil.isHttpSuccess(jsonResponse.getInt(Constants.STATUS_CODE))) {

				return true;
			} else {

				errorMsg = jsonResponse.getString(Constants.STATUS_MSG);
			}

		} catch (Exception e) {

			errorMsg = AndroidUtil.getString(wItemListActivity.get(), R.string.error_unknown_error);
			Log.e(TAG, e.getMessage());
		}

		return false;
	}

	@Override
	protected void onPostExecute(final Boolean success) {

		ItemListActivity itemListActivity = wItemListActivity.get();

		if (Validator.isNotNull(itemListActivity) && success) {

			Cart.getInstance().removeItem(cartItemId, marketItem);

			itemListActivity.refreshCartView();

		} else if (Validator.isNotNull(itemListActivity) && !success) {

			DialogUtil.showAlertDialog(itemListActivity, itemListActivity.getString(R.string.error),
					errorMsg + itemListActivity.getString(R.string.removing_item_from_cart));
		}
	}
}