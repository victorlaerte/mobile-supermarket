package com.victorlaerte.supermarket;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import com.victorlaerte.supermarket.model.Token;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.model.impl.TokenImpl;
import com.victorlaerte.supermarket.model.impl.UserImpl;
import com.victorlaerte.supermarket.util.Constants;

import junit.framework.Assert;

public class UserUnitTest {

	private String id;
	private String name;
	private String userName;
	private String email;
	private Token token;

	private JSONObject userJSON;

	@Before
	public void setUp() throws Exception {

		id = RandomStringUtils.randomNumeric(10);
		name = RandomStringUtils.randomAlphabetic(10);
		userName = RandomStringUtils.randomAlphabetic(10);
		email = "teste@teste.com";
		token = new TokenImpl(RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphabetic(10),
				RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));

		userJSON = new JSONObject();

		userJSON.put(Constants.ID, id);
		userJSON.put(Constants.NAME, name);
		userJSON.put(Constants.USERNAME, userName);
		userJSON.put(Constants.EMAIL, email);
		userJSON.put(Constants.TOKEN, new JSONObject(token.toString()));
	}

	@Test
	public void testToString() {

		User user = new UserImpl(id, name, userName, email, token);

		try {

			assertEqualJSON(user);

		} catch (JSONException e) {

			Assert.fail();
		}
	}

	@Test
	public void testFromJSON() {

		try {

			User userFromJSON = new UserImpl(userJSON);

			assertEqualJSON(userFromJSON);

		} catch (JSONException e) {

			Assert.fail();
		}
	}

	private void assertEqualJSON(User user) throws JSONException {

		JSONObject jsonObject = new JSONObject(user.toString());

		String id = jsonObject.getString(Constants.ID);
		String name = jsonObject.getString(Constants.NAME);
		String userName = jsonObject.getString(Constants.USERNAME);
		String email = jsonObject.getString(Constants.EMAIL);

		// @formatter:off
		Assert.assertTrue(userJSON.getString(Constants.ID).equals(id) &&
						  userJSON.getString(Constants.NAME).equals(name) &&
						  userJSON.getString(Constants.USERNAME).equals(userName) &&
						  userJSON.getString(Constants.EMAIL).equals(email));
		// @formatter:on
	}
}