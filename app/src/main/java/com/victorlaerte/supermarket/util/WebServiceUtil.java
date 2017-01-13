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
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Pair;

public class WebServiceUtil {

	private static final String LOG_TAG = WebServiceUtil.class.getName();
	private static final String SSL_PROTOCOL = "TLS";

	private static InputStream readInputStream(String uri, HttpMethod httpMethod, Map<String, String> paramsMap)
			throws MalformedURLException, IOException {

		HttpURLConnection connection = null;

		InputStream in = null;

		try {

			/*
			 * FIX: this workaround is to disable SSL certificate check, only for this test. This should me removed in production mode 
			 *
			
			SSLContext sc = SSLContext.getInstance(SSL_PROTOCOL);
			sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			
				public boolean verify(String string, SSLSession ssls) {
			
					return true;
				}
			});
			*/
			URL url = new URL(uri);

			connection = (HttpURLConnection) url.openConnection();

			connection.setReadTimeout(10000);
			connection.setConnectTimeout(20000);
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");

			connection.setDoInput(true);
			connection.setDoOutput(true);

			String query = StringPool.BLANK;

			if (Validator.isNotNull(paramsMap) && !paramsMap.isEmpty()) {

				Uri.Builder builder = new Uri.Builder();

				for (Map.Entry<String, String> entry : paramsMap.entrySet()) {

					builder.appendQueryParameter(entry.getKey(), entry.getValue());
				}

				query = builder.build().getEncodedQuery();
			}

			if (!query.isEmpty()) {

				OutputStream os = connection.getOutputStream();

				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StringPool.UTF8));

				writer.write(query);
				writer.flush();
				writer.close();

				os.close();

			}

			connection.connect();

			int status = connection.getResponseCode();

			if (status == 200) {

				in = connection.getInputStream();
			} else {
				in = connection.getErrorStream();
			}

		} finally {

			connection.disconnect();
		}

		return in;
	}

	private static String getQuery(List<Pair<String, String>> params) throws UnsupportedEncodingException {

		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (Pair<String, String> pair : params) {

			if (first) {
				first = false;
			} else {
				result.append(StringPool.AMPERSAND);
			}

			result.append(URLEncoder.encode(pair.first, StringPool.UTF8));
			result.append(StringPool.EQUAL);
			result.append(URLEncoder.encode(pair.second, StringPool.UTF8));
		}

		return result.toString();
	}

	public static JSONObject readJSONResponse(String uri, HttpMethod httpMethod, Map<String, String> paramsMap)
			throws IOException, ParserConfigurationException, JSONException {

		JSONObject jsonObject = new JSONObject();

		try (InputStream in = readInputStream(uri, httpMethod, paramsMap)) {

			String str = IOUtils.toString(in, StringPool.UTF8);

			jsonObject = new JSONObject(str);
		}

		return jsonObject;
	}

	private static class TrustAllX509TrustManager implements X509TrustManager {

		public X509Certificate[] getAcceptedIssuers() {

			return new X509Certificate[0];
		}

		public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}

		public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}

	}
}
