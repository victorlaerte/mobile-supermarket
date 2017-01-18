package com.victorlaerte.supermarket.util;

/**
 * Created by Victor on 11/01/2017.
 */

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class WebServiceUtil {

	private static final String TAG = WebServiceUtil.class.getName();

	public static JSONObject readJSONResponse(String uri, HttpMethod httpMethod, Map<String, String> paramsMap)
			throws MalformedURLException, IOException, JSONException {

		return readJSONResponse(uri, httpMethod, paramsMap, false, StringPool.BLANK);
	}

	public static JSONObject readJSONResponse(String uri, HttpMethod httpMethod, Map<String, String> paramsMap,
			boolean basicAuth, String authString) throws MalformedURLException, IOException, JSONException {

		JSONObject jsonObject = new JSONObject();

		HttpURLConnection connection = null;

		InputStream in = null;

		String strResponse = StringPool.BLANK;

		try {

			String query = StringPool.BLANK;

			if (Validator.isNotNull(paramsMap) && !paramsMap.isEmpty()) {

				query = getQuery(paramsMap);
			}

			if (!query.isEmpty() && !httpMethod.equals(HttpMethod.POST)) {

				uri += StringPool.QUESTION + query;
			}

			URL url = new URL(uri);

			connection = (HttpURLConnection) url.openConnection();

			if (basicAuth && Validator.isNotNull(authString) && !authString.isEmpty()) {

				connection.setRequestProperty(Constants.AUTHORIZATION, authString);
			}

			connection.setRequestMethod(httpMethod.toString());
			connection.setReadTimeout(10000);
			connection.setConnectTimeout(20000);
			connection.setDoInput(true);

			if (httpMethod.equals(HttpMethod.POST)) {

				connection.setDoOutput(true);
			}

			if (!query.isEmpty() && httpMethod.equals(HttpMethod.POST)) {

				OutputStream os = connection.getOutputStream();

				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StringPool.UTF8));

				writer.write(query);
				writer.flush();

				IOUtils.closeQuietly(writer);
				IOUtils.closeQuietly(os);

			}

			connection.connect();

			int statusCode = connection.getResponseCode();
			String statusMsg = connection.getResponseMessage();

			// HTTP Response Success 
			if (isHttpSuccess(statusCode)) {

				in = connection.getInputStream();
			} else {
				in = connection.getErrorStream();
			}

			strResponse = IOUtils.toString(in, StringPool.UTF8);

			if (strResponse.length() > 0) {

				Object json = new JSONTokener(strResponse).nextValue();

				if (json instanceof JSONObject) {

					jsonObject.put(Constants.BODY, (JSONObject) json);

				} else if (json instanceof JSONArray) {

					jsonObject.put(Constants.BODY, (JSONArray) json);
				}
			}

			jsonObject.put(Constants.STATUS_CODE, statusCode);
			jsonObject.put(Constants.STATUS_MSG, statusMsg);

		} finally {

			IOUtils.closeQuietly(in);
			connection.disconnect();
		}

		return jsonObject;
	}

	private static String getQuery(Map<String, String> paramsMap) throws UnsupportedEncodingException {

		StringBuilder sb = new StringBuilder();

		for (Map.Entry<String, String> entry : paramsMap.entrySet()) {

			sb.append(URLEncoder.encode(entry.getKey(), StringPool.UTF8));
			sb.append(StringPool.EQUAL);
			sb.append(URLEncoder.encode(entry.getValue(), StringPool.UTF8));
			sb.append(StringPool.AMPERSAND);
		}

		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}

		return sb.toString();
	}

	public static boolean isHttpSuccess(int statusCode) {

		if (statusCode > 199 && statusCode < 300) {
			return true;
		} else {
			return false;
		}
	}

}
