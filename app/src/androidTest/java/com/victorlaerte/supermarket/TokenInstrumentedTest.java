package com.victorlaerte.supermarket;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.victorlaerte.supermarket.model.Token;
import com.victorlaerte.supermarket.model.impl.TokenImpl;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

@RunWith(AndroidJUnit4.class)
public class TokenInstrumentedTest {

	private String accessToken;
	private String refreshToken;
	private String scope;
	private String tokenType;

	@Before
	public void setUp() throws Exception {

		accessToken = RandomStringUtils.randomAlphanumeric(10);
		refreshToken = RandomStringUtils.randomAlphabetic(10);
		scope = RandomStringUtils.randomAlphabetic(10);
		tokenType = RandomStringUtils.randomAlphabetic(10);
	}

	@Test
	public void testParcelable() {

		Token token = new TokenImpl(accessToken, refreshToken, scope, tokenType);

		Parcel parcel = Parcel.obtain();
		token.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		Token parceledToken = TokenImpl.CREATOR.createFromParcel(parcel);

		_assertEquals(token, parceledToken);
	}

	private void _assertEquals(Token oldToken, Token newToken) {

		// @formatter:off
		Assert.assertTrue(oldToken.getAccessToken().equals(newToken.getAccessToken()) &&
				oldToken.getRefresToken().equals(newToken.getRefresToken()) &&
				oldToken.getScope().equals(newToken.getScope()) &&
				oldToken.getTokenType().equals(newToken.getTokenType()));
		// @formatter:on
	}
}
