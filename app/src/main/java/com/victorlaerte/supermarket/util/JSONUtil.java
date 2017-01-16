package com.victorlaerte.supermarket.util;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Created by victoroliveira on 15/01/17.
 */

public class JSONUtil {

	private static final String TAG = JSONUtil.class.getName();

	public static JSONObject createJSONErrorResponseHTTPLike(Exception e) {

		JSONObject response = new JSONObject();

		try {

			response.put(Constants.STATUS_CODE, 0);
			response.put(Constants.STATUS_MSG, e.getMessage());

		} catch (JSONException ex) {

			Log.e(TAG, ex.getMessage());
		}

		return response;
	}
}
