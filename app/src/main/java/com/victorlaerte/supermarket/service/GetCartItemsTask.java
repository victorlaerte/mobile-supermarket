package com.victorlaerte.supermarket.service;

/**
 * Created by victoroliveira on 12/01/17.
 */

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.victorlaerte.supermarket.model.Cart;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.model.impl.CartItemImpl;
import com.victorlaerte.supermarket.model.impl.MarketItemImpl;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.HttpMethod;
import com.victorlaerte.supermarket.util.HttpUtil;
import com.victorlaerte.supermarket.util.StringPool;
import com.victorlaerte.supermarket.util.SuperMarketUtil;
import com.victorlaerte.supermarket.util.Validator;
import com.victorlaerte.supermarket.view.ItemListActivity;

import android.os.AsyncTask;
import android.util.Log;

public class GetCartItemsTask extends AsyncTask<String, String, Boolean> {

	private static final String TAG = GetCartItemsTask.class.getName();
	private WeakReference<ItemListActivity> wItemListActivity;
	private String url = Constants.DATA_BASE_URL + Constants.CART_ENDPOINT;
	private User user;

	public GetCartItemsTask(ItemListActivity itemListActivity, User user) {

		wItemListActivity = new WeakReference<ItemListActivity>(itemListActivity);
		this.user = user;
	}

	protected Boolean doInBackground(String... params) {

		Map<String, String> httpParams = new HashMap<String, String>();

		try {

			JSONObject jsonResponse = HttpUtil.sendRequest(url, HttpMethod.GET, httpParams,
					SuperMarketUtil.getAuthString(user.getToken().getAccessToken()));

			Log.d(TAG, jsonResponse.toString());

			if (HttpUtil.isHttpSuccess(jsonResponse.getInt(Constants.STATUS_CODE))) {

				JSONArray body = jsonResponse.getJSONArray(Constants.BODY);

				Cart.getInstance().clear();

				for (int i = 0; i < body.length(); i++) {

					JSONObject currentCartItem = body.getJSONObject(i);

					try {

						String cartItemId = currentCartItem.getString("id");
						String userId = currentCartItem.getString("userId");

						String productTitle = currentCartItem.getString("productTitle");
						String productFilename = currentCartItem.getString("productFilename");
						String productId = currentCartItem.getString("productId");

						Double price = currentCartItem.getDouble("productPrice");

						MarketItemImpl marketItem = new MarketItemImpl(productId, productTitle, productTitle,
								StringPool.BLANK, price, 0, productFilename, 200, 200);

						Cart.getInstance().addItem(new CartItemImpl(cartItemId, marketItem));

					} catch (JSONException ex) {

						Log.e(TAG, ex.getMessage());
					}
				}

				return true;

			}

		} catch (Exception e) {

			Log.e(TAG, e.getMessage());
		}

		return false;
	}

	@Override
	protected void onPostExecute(final Boolean success) {

		ItemListActivity itemListActivity = wItemListActivity.get();

		if (Validator.isNotNull(itemListActivity) && success) {

			itemListActivity.refreshCartView();
		}
	}
}