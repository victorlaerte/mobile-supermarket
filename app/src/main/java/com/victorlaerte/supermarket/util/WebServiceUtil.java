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
import org.json.JSONException;
import org.json.JSONObject;

public class WebServiceUtil {

	private static final String LOG_TAG = WebServiceUtil.class.getName();

	public static JSONObject readJSONResponse(String uri, HttpMethod httpMethod, Map<String, String> paramsMap)
			throws MalformedURLException, IOException, JSONException {

		JSONObject jsonObject = new JSONObject();

		HttpURLConnection connection = null;

		InputStream in = null;

		String strResponse = StringPool.BLANK;

		try {

			String query = StringPool.BLANK;

			if (Validator.isNotNull(paramsMap) && !paramsMap.isEmpty()) {

				query = getQuery(paramsMap);
			}

			URL url = new URL(uri);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod(httpMethod.toString());
			connection.setReadTimeout(10000);
			connection.setConnectTimeout(20000);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			if (!query.isEmpty()) {

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
			if (statusCode > 199 && statusCode < 300) {

				in = connection.getInputStream();
			} else {
				in = connection.getErrorStream();
			}

			strResponse = IOUtils.toString(in, StringPool.UTF8);

			jsonObject = new JSONObject(strResponse);

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

}
