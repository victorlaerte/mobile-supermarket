package com.victorlaerte.supermarket;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.victorlaerte.supermarket.model.Token;
import com.victorlaerte.supermarket.model.User;
import com.victorlaerte.supermarket.model.impl.TokenImpl;
import com.victorlaerte.supermarket.model.impl.UserImpl;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

@RunWith(AndroidJUnit4.class)
public class UserInstrumentedTest {

	private String id;
	private String name;
	private String userName;
	private String email;
	private Token token;

	@Before
	public void setUp() throws Exception {

		id = RandomStringUtils.randomNumeric(10);
		name = RandomStringUtils.randomAlphabetic(10);
		userName = RandomStringUtils.randomAlphabetic(10);
		email = "teste@teste.com";
		token = new TokenImpl(RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphabetic(10),
				RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
	}

	@Test
	public void testParcelable() {

		User user = new UserImpl(id, name, userName, email, token);

		Parcel parcel = Parcel.obtain();
		user.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);

		User parceledUser = UserImpl.CREATOR.createFromParcel(parcel);

		_assertEquals(user, parceledUser);
	}

	private void _assertEquals(User olderUser, User newUser) {

		// @formatter:off
		Assert.assertTrue(olderUser.getId().equals(newUser.getId()) &&
						  olderUser.getName().equals(newUser.getName()) &&
						  olderUser.getUserName().equals(newUser.getUserName()) &&
						  olderUser.getEmail().equals(newUser.getEmail()));
		// @formatter:on
	}

}
