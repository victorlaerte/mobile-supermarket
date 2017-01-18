package com.victorlaerte.supermarket.service;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.victorlaerte.supermarket.R;
import com.victorlaerte.supermarket.model.MarketItem;
import com.victorlaerte.supermarket.model.TypeFilter;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.model.impl.MarketItemImpl;
import com.victorlaerte.supermarket.util.AndroidUtil;
import com.victorlaerte.supermarket.util.Constants;
import com.victorlaerte.supermarket.util.HttpMethod;
import com.victorlaerte.supermarket.util.HttpUtil;
import com.victorlaerte.supermarket.util.StringPool;
import com.victorlaerte.supermarket.util.SuperMarketUtil;
import com.victorlaerte.supermarket.util.Validator;
import com.victorlaerte.supermarket.view.ItemListActivity;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by victoroliveira on 16/01/17.
 */

public class GetMarketItemsTask extends AsyncTask<Void, Void, Boolean> {

	private static final String TAG = GetMarketItemsTask.class.getName();
	private final User user;
	private WeakReference<ItemListActivity> wItemListActivity;
	private String url = Constants.DATA_BASE_URL + Constants.GET_PRODUCTS_ENDPOINT;
	private List<MarketItem> marketItemList = new ArrayList<MarketItem>();
	private String errorMsg = StringPool.BLANK;
	private TypeFilter typeFilter;

	public GetMarketItemsTask(ItemListActivity itemListActivity, User user, TypeFilter typeFilter) {

		wItemListActivity = new WeakReference<ItemListActivity>(itemListActivity);
		this.user = user;
		this.typeFilter = typeFilter;
	}

	@Override
	protected Boolean doInBackground(Void... params) {

		try {

			Map<String, String> mapParams = new HashMap<String, String>();

			if (Validator.isNotNull(typeFilter)) {

				mapParams.put(Constants.FILTER, getFilterJSONAsString(typeFilter));
			}

			JSONObject jsonResponse = HttpUtil.sendRequest(url, HttpMethod.GET, mapParams,
					SuperMarketUtil.getAuthString(user.getToken().getAccessToken()));

			Log.d(TAG, jsonResponse.toString());

			if (HttpUtil.isHttpSuccess(jsonResponse.getInt(Constants.STATUS_CODE))) {

				JSONArray jArrayItem = jsonResponse.getJSONArray(Constants.BODY);

				for (int i = 0; i < jArrayItem.length(); i++) {

					JSONObject currentJSONItem = jArrayItem.getJSONObject(i);

					try {

						MarketItem marketItem = new MarketItemImpl(currentJSONItem);
						marketItemList.add(marketItem);

					} catch (JSONException ex) {

						Log.e(TAG, ex.getMessage());
					}
				}

				return true;

			} else {

				errorMsg = jsonResponse.getString(Constants.STATUS_MSG);
			}

		} catch (Exception e) {

			if (Validator.isNotNull(wItemListActivity.get())) {

				errorMsg = AndroidUtil.getString(wItemListActivity.get(), R.string.error_unknown_error);
			}

			Log.e(TAG, e.getMessage());
		}

		return false;
	}

	private String getFilterJSONAsString(TypeFilter typeFilter) {

		StringBuilder sb = new StringBuilder(StringPool.OPEN_CURLY_BRACE);
		sb.append("\"" + Constants.TYPE + "\"");
		sb.append(StringPool.COLON);
		sb.append("\"" + typeFilter.getLabel() + "\"");
		sb.append(StringPool.CLOSE_CURLY_BRACE);

		return sb.toString();
	}

	@Override
	protected void onPostExecute(final Boolean success) {

		ItemListActivity itemListActivity = wItemListActivity.get();

		if (Validator.isNotNull(itemListActivity)) {

			itemListActivity.setupRecyclerView(success, errorMsg, marketItemList);
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
