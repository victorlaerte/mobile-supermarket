package com.victorlaerte.supermarket.service;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.victorlaerte.supermarket.model.CartItem;
import com.victorlaerte.supermarket.model.ProductStockContent;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.model.impl.CartItemImpl;
import com.victorlaerte.supermarket.model.impl.ProductStockContentImpl;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.HttpMethod;
import com.victorlaerte.supermarket.util.StringPool;
import com.victorlaerte.supermarket.util.SuperMarketUtil;
import com.victorlaerte.supermarket.util.Validator;
import com.victorlaerte.supermarket.util.WebServiceUtil;
import com.victorlaerte.supermarket.view.ItemListActivity;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by victoroliveira on 16/01/17.
 */

public class GetProductsTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = GetProductsTask.class.getName();
	private final User user;
	private WeakReference<ItemListActivity> wItemListActivity;
	private String url = Constants.DATA_BASE_URL + Constants.GET_PRODUCTS_ENDPOINT;
	private ProductStockContent productStockContent;
	private String errorMsg = StringPool.BLANK;

	public GetProductsTask(ItemListActivity itemListActivity, User user) {

		wItemListActivity = new WeakReference<ItemListActivity>(itemListActivity);
		this.user = user;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try {

			JSONObject jsonResponse = WebServiceUtil.readJSONResponse(url, HttpMethod.GET, null, true,
					SuperMarketUtil.getAuthString(user.getToken().getAccessToken()));

			Log.d(TAG, jsonResponse.toString());

			List<CartItem> cartItemList = new ArrayList<CartItem>();

			if (jsonResponse.getInt(Constants.STATUS_CODE) == 200) {

				JSONArray jArrayItem = jsonResponse.getJSONArray(Constants.BODY);

				for (int i = 0; i < jArrayItem.length(); i++) {

					JSONObject currentJSONItem = jArrayItem.getJSONObject(i);

					try {

						CartItem cartItem = new CartItemImpl(currentJSONItem);
						cartItemList.add(cartItem);

					} catch (JSONException ex) {

						Log.e(TAG, ex.getMessage());
					}

				}

				productStockContent = new ProductStockContentImpl(cartItemList);

				return true;
				
			} else {

				errorMsg = jsonResponse.getString(Constants.STATUS_MSG);
			}

		} catch (Exception e) {

			errorMsg = e.getMessage();
			Log.e(TAG, e.getMessage());
		}

		return false;
	}

	@Override
	protected void onPostExecute(final Boolean success) {

		ItemListActivity itemListActivity = wItemListActivity.get();

		if (Validator.isNotNull(itemListActivity)) {

			itemListActivity.setupRecyclerView(success, errorMsg, productStockContent);
		}
	}

	@Override
	protected void onCancelled() {

		ItemListActivity itemListActivity = wItemListActivity.get();

		if (Validator.isNotNull(itemListActivity)) {

			itemListActivity.onGetProductsCanceled();
		}
	}
}
