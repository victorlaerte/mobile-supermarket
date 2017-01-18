package com.victorlaerte.supermarket;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.victorlaerte.supermarket.model.Token;
import com.victorlaerte.supermarket.model.impl.TokenImpl;
import com.victorlaerte.supermarket.util.Constants;

import android.os.Parcel;

import junit.framework.Assert;

public class TokenUnitTest {

	private String accessToken;
	private String refreshToken;
	private String scope;
	private String tokenType;

	private JSONObject tokenJSON;

	@Before
	public void setUp() throws Exception {

		accessToken = RandomStringUtils.randomAlphanumeric(10);
		refreshToken = RandomStringUtils.randomAlphabetic(10);
		scope = RandomStringUtils.randomAlphabetic(10);
		tokenType = RandomStringUtils.randomAlphabetic(10);

		tokenJSON = new JSONObject();

		tokenJSON.put(Constants.ACCESS_TOKEN, accessToken);
		tokenJSON.put(Constants.REFRESH_TOKEN, refreshToken);
		tokenJSON.put(Constants.SCOPE, scope);
		tokenJSON.put(Constants.TOKEN_TYPE, tokenType);
	}

	@Test
	public void testToString() {

		Token token = new TokenImpl(accessToken, refreshToken, scope, tokenType);

		try {

			assertEqualJSON(token);

		} catch (JSONException e) {

			Assert.fail();
		}
	}

	@Test
	public void testFromJSON() {

		try {

			Token tokenFromJSON = new TokenImpl(tokenJSON);

			assertEqualJSON(tokenFromJSON);

		} catch (JSONException e) {

			Assert.fail();
		}

	}

	private void assertEqualJSON(Token token) throws JSONException {

		JSONObject jsonObject = new JSONObject(token.toString());

		String accessToken = jsonObject.getString(Constants.ACCESS_TOKEN);
		String refreshToken = jsonObject.getString(Constants.REFRESH_TOKEN);
		String scope = jsonObject.getString(Constants.SCOPE);
		String tokenType = jsonObject.getString(Constants.TOKEN_TYPE);

		// @formatter:off
		Assert.assertTrue(tokenJSON.getString(Constants.ACCESS_TOKEN).equals(accessToken) &&
						  tokenJSON.getString(Constants.REFRESH_TOKEN).equals(refreshToken) &&
						  tokenJSON.getString(Constants.SCOPE).equals(scope) &&
						  tokenJSON.getString(Constants.TOKEN_TYPE).equals(tokenType));
		// @formatter:on
	}
}